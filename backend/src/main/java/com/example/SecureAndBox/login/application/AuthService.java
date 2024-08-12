package com.example.SecureAndBox.login.application;

import java.io.IOException;

import com.example.SecureAndBox.login.dto.response.JwtTokenResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;
import org.webjars.NotFoundException;

import com.example.SecureAndBox.entity.User;
import com.example.SecureAndBox.login.domain.Constants;
import com.example.SecureAndBox.login.domain.enums.Provider;
import com.example.SecureAndBox.login.dto.SocialInfoDto;

import com.example.SecureAndBox.login.dto.request.LoginRequestDto;
import com.example.SecureAndBox.login.dto.request.SignUpDto;
import com.example.SecureAndBox.login.dto.response.JwtTokenResponse;
import com.example.SecureAndBox.login.exception.NotFoundUserInfoException;
import com.example.SecureAndBox.oauth.dto.KakaoTokenResponse;
import com.example.SecureAndBox.oauth.security.info.UserAuthentication;
import com.example.SecureAndBox.oauth.utils.JwtUtil;
import com.example.SecureAndBox.repository.UserRepository;

import io.jsonwebtoken.security.InvalidKeyException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

	private final KakaoLoginService kakaoLoginService;
	private final UserRepository userRepository;
	private final JwtUtil jwtUtil;
	private final RestTemplate restTemplate;
	@Autowired
	private  PasswordEncoder passwordEncoder;
	@Transactional
	public JwtTokenResponseDto login(KakaoTokenResponse providerToken, LoginRequestDto request) throws IOException {
		SocialInfoDto socialInfo = getSocialInfo(request, providerToken.getAccess_token());
		User user = loadOrCreateUser(request.provider(), socialInfo);
		String refreshToken = providerToken.getRefresh_token();
		if (refreshToken == null) {
			refreshToken = user.getRefreshToken();
		}
		return generateTokensWithUpdateRefreshToken(user, providerToken.getAccess_token(), refreshToken);
	}

	private SocialInfoDto getSocialInfo(LoginRequestDto request, String providerToken) {
		if (request.provider().toString().equals(Provider.KAKAO.toString())) {
			return kakaoLoginService.getInfo(providerToken);
		} else {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Social login provider not supported: " + request.provider());
		}
	}

	private User loadOrCreateUser(Provider provider, SocialInfoDto socialInfo) {
		return userRepository.findBySerialId(socialInfo.serialId())
			.orElseGet(() -> {
				User newUser = User.builder()
					.serialId(socialInfo.serialId())
				//	.email(socialInfo.email())
					.name(socialInfo.name())
					.role(User.Role.USER)
					.refreshToken("") // Initialize refreshToken as empty string
					.build();
				userRepository.save(newUser);
				return newUser;
			});
	}

	public JwtTokenResponseDto notSocialLogin(String username, String rawPassword) {
		User user = userRepository.findByUsername(username)
			.orElseThrow(() -> new NotFoundException("존재하지 않는 아이디 입니다."));

		// 비밀번호 비교
		if (!passwordEncoder.matches(rawPassword, user.getPw())) {
			throw new InvalidKeyException("올바르지 않은 비밀번호입니다.");
		}

		JwtTokenResponseDto jwtTokenResponse = jwtUtil.generateTokensBypw(
			user.getUserId(),
			user.getRole(),
			user.getPw(),
			user.getRefreshToken()
		);
		return jwtTokenResponse;
	}



	public void createUser(SignUpDto dto)
	{   String encodepw=passwordEncoder.encode(dto.getPw());
		User user = User.builder()
			.username(dto.getUsername())
			.pw(encodepw)
			.name(dto.getUsername())
			.email(dto.getEmail())
			.serialId("NONE")
			.refreshToken("NONE")
			.role(User.Role.USER)
			.build();
		userRepository.save(user);
	}








	private JwtTokenResponseDto generateTokensWithUpdateRefreshToken(User user, String accessToken, String refreshToken) {
		JwtTokenResponseDto jwtTokenResponse = jwtUtil.generateTokens(user.getUserId(), user.getRole(), accessToken,
			refreshToken);
		user.updateRefreshToken(refreshToken);
		return jwtTokenResponse;
	}

	private String getToken(String token) {
		if (token.startsWith(Constants.BEARER_PREFIX)) {
			return token.substring(Constants.BEARER_PREFIX.length());
		} else {
			return token;
		}
	}

	@Transactional
	public void logout(UserAuthentication authentication) {
		String accessToken = jwtUtil.getOriginalAccessToken(authentication.getAccessToken());
		String logoutUrl = "https://kapi.kakao.com/v1/user/logout";
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		headers.set("Authorization", "Bearer " + accessToken);

		HttpEntity<String> entity = new HttpEntity<>(headers);
		restTemplate.exchange(logoutUrl, HttpMethod.POST, entity, String.class);
		try {
			Long userId = Long.parseLong(authentication.getName());
			User user = userRepository.findById(userId).get();
			user.updateRefreshToken("");
		} catch (HttpClientErrorException e) {
			throw new RuntimeException("HTTP error while getting access token from Kakao: " + e.getStatusCode() + " - "
				+ e.getResponseBodyAsString(), e);
		} catch (Exception e) {
			throw new RuntimeException("Unexpected error while getting access token from Kakao", e);
		}

	}

}
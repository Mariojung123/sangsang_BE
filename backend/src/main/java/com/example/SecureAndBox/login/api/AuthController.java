package com.example.SecureAndBox.login.api;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

import com.example.SecureAndBox.login.dto.response.JwtTokenResponseDto;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.webjars.NotFoundException;

import com.example.SecureAndBox.login.application.AuthService;
import com.example.SecureAndBox.login.application.KakaoLoginService;
import com.example.SecureAndBox.login.application.UserService;
import com.example.SecureAndBox.login.domain.enums.Provider;
import com.example.SecureAndBox.login.dto.request.LoginDto;
import com.example.SecureAndBox.login.dto.request.LoginRequestDto;
import com.example.SecureAndBox.login.dto.request.SignUpDto;
import com.example.SecureAndBox.login.dto.response.JwtTokenResponse;
import com.example.SecureAndBox.oauth.dto.KakaoTokenResponse;
import com.example.SecureAndBox.oauth.security.info.UserAuthentication;

import io.jsonwebtoken.security.InvalidKeyException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/oauth")
@RequiredArgsConstructor
@Tag(name = "Auth", description = "카카오 로그인 api / 담당자 : 이영학")
public class AuthController {

	private final AuthService authService;
	private final KakaoLoginService kakaoLoginService;


	private static final int MAX_ATTEMPTS = 5;
	private static final long LOCK_TIME_DURATION = 15 * 60 * 1000; // 15 minutes

	private Map<String, Integer> loginAttempts = new HashMap<>();
	private Map<String, Long> lockTime = new HashMap<>();

	private static final Logger logger = Logger.getLogger(AuthController.class.getName());


	@Value("${kakao.api.key}")
	private String apiKey;
	@Value("${kakao.redirect.url}")
	private String redirectUri;// Replace with your actual redirect URI
	@Operation(summary = "카카오 로그인 code 가져오기")
	@GetMapping("")
	public ResponseEntity<?> redirectToKakaoLogin(HttpServletResponse response) throws IOException {
		String clientId = apiKey;  // Replace with your Kakao REST API Key

		String kakaoAuthUrl = "https://kauth.kakao.com/oauth/authorize"
			+ "?response_type=code"
			+ "&client_id=" + clientId
			+ "&redirect_uri=" + URLEncoder.encode(redirectUri, StandardCharsets.UTF_8.toString())
			+ "&scope=profile_nickname";

		// Set CORS headers
		response.setHeader("Access-Control-Allow-Origin", "http://localhost:4200");
		response.setHeader("Access-Control-Allow-Credentials", "true");

		// Perform the redirect
		Map<String, String> responseBody = new HashMap<>();
		responseBody.put("redirectUrl", kakaoAuthUrl);
		return ResponseEntity.ok(responseBody);
	}
	@Operation(summary = "로그인 -> 아이디 로그인")
	@PostMapping("/login")
	public ResponseEntity<?> login(@Valid @RequestBody LoginDto dto) {
		ResponseEntity<?> response;
		String username = dto.getUsername();
		String password =dto.getPw();


		if (isAccountLocked(username)) {
			return ResponseEntity.status(HttpStatus.LOCKED).body("Account is locked. Please try again later.");
		}

		try {
			if (authenticate(username, password)) {

				JwtTokenResponseDto jwtTokenResponse = authService.notSocialLogin(username, password);
				resetAttempts(username);
				return ResponseEntity.ok(jwtTokenResponse);
			} else {
				incrementAttempts(username);
				if (loginAttempts.get(username) >= MAX_ATTEMPTS) {
					lockAccount(username);
					return ResponseEntity.status(HttpStatus.LOCKED).body("Account locked due to too many failed attempts.");
				}
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials.");
			}
		} catch (Exception e) {
			response =  ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred.");
		}
		return response;
	}

	private boolean isAccountLocked(String username) {
		Long lockTime = this.lockTime.get(username);
		if (lockTime == null) return false;

		if (System.currentTimeMillis() - lockTime > LOCK_TIME_DURATION) {
			this.lockTime.remove(username);
			this.loginAttempts.remove(username);
			return false;
		}
		return true;
	}

	private boolean authenticate(String username, String pw)
	{
		if (!isValidId(username) || !isValidPassword(pw)) {
			return false;
		}

		return true;
	}
	private void incrementAttempts(String username) {
		loginAttempts.put(username, loginAttempts.getOrDefault(username, 0) + 1);
	}

	private void resetAttempts(String username) {
		loginAttempts.remove(username);
		lockTime.remove(username);
	}

	private void lockAccount(String username) {
		lockTime.put(username, System.currentTimeMillis());
	}
	@Operation(summary = "회원가입")
	@PostMapping("/signUp")
	public ResponseEntity<?> signUp(
		@Valid @RequestBody SignUpDto dto
	){
		String id = dto.getUsername();
		String pw = dto.getPw();

		if (!isValidId(id) || !isValidPassword(pw)) {
			return ResponseEntity.badRequest().body("Invalid ID or Password format");
		}
		authService.createUser(dto);
		return ResponseEntity.ok("회원가입 되었습니다.");
	}

	private boolean isValidId(String id) {
		// ID 유효성 검사 로직 (예: 영숫자만 허용, 5~20자 사이)
		String idPattern = "^[a-zA-Z0-9]{5,20}$";
		return Pattern.matches(idPattern, id);
	}

	private boolean isValidPassword(String pw) {
		// 비밀번호 유효성 검사 로직 (예: 영숫자와 특수문자만 허용, 8~30자 사이)
		String passwordPattern = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,30}$";
		return Pattern.matches(passwordPattern, pw);
	}

	@Operation(summary = "카카오 로그인 토큰 받아오기 -> 인가코드 주입하고 토큰 받기")
	@GetMapping("/callback")
	public ResponseEntity<String> kakaoCallback(@RequestParam String code) {

		ResponseEntity<String> response;
		try {
			// 카카오 API를 사용하여 액세스 토큰 및 리프레시 토큰 가져오기
			KakaoTokenResponse accessToken = kakaoLoginService.getAccessToken(code, apiKey, redirectUri);
			LoginRequestDto request = new LoginRequestDto(Provider.KAKAO, null); // Name은 여기서 null로 설정
			//sparrow - 로그인 루틴은 반복 구문을 통해 수행 횟수가 제어되어야 한다.
			// JWT 토큰 생성
			JwtTokenResponseDto tokens = authService.login(accessToken, request);

			// 토큰 정보를 JavaScript에 주입하여 HTML 반환
			String htmlContent = String.format("""
				<script>
				    const userInfo = {
				        method: 'kakao',
				        accessToken: '%s',
				        refreshToken: '%s',
				    };
				    window.opener.postMessage(userInfo, "*");
				</script>%n""", tokens.getAccessToken(), tokens.getRefreshToken());

			response = ResponseEntity.ok()
				.contentType(MediaType.TEXT_HTML)
				.body(htmlContent);

		} catch (AuthenticationException e) {
			logger.log(Level.WARNING, "Authentication failed: " + e.getMessage(), e);
			response = ResponseEntity.status(HttpStatus.UNAUTHORIZED)
				.body("<script>alert('Authentication failed.'); window.close();</script>");

		} catch (IOException e) {
			logger.log(Level.SEVERE, "IO error during Kakao callback processing: " + e.getMessage(), e);
			response = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
				.body("<script>alert('Internal server error.'); window.close();</script>");

		} catch (Exception e) {
			logger.log(Level.SEVERE, "Unexpected error during Kakao callback processing: " + e.getMessage(), e);
			response = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
				.body("<script>alert('An unexpected error occurred.'); window.close();</script>");
		}
		return response;
	}


	@Operation(summary = "카카오 로그아웃 하기")
	@PostMapping("/logout")
	public ResponseEntity<?> logout() {
		UserAuthentication authentication = (UserAuthentication)SecurityContextHolder.getContext().getAuthentication();
		authService.logout(authentication);
		return ResponseEntity.ok("로그아웃에 성공하였습니다.");
	}
	@Operation(summary = "카카오 로그인 리프레시 토큰 받기")
	@PostMapping("/refresh-kakao-token")
	public ResponseEntity<?> refreshKakaoToken(@RequestParam String refreshToken) throws IOException {
		KakaoTokenResponse response = kakaoLoginService.refreshKakaoToken(refreshToken);
		LoginRequestDto request = new LoginRequestDto(Provider.KAKAO, null);
		//sparrow - 로그인 루틴은 반복 구문을 통해 수행 횟수가 제어되어야 한다.
		JwtTokenResponseDto tokens = authService.login(response, request);
		return ResponseEntity.ok((tokens));
	}

}

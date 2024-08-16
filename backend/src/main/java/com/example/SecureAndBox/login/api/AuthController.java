package com.example.SecureAndBox.login.api;

import static com.example.SecureAndBox.login.exception.LoginExceptionCode.*;

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

import com.example.SecureAndBox.login.application.AuthService;
import com.example.SecureAndBox.login.application.KakaoLoginService;
import com.example.SecureAndBox.login.application.UserService;
import com.example.SecureAndBox.login.domain.enums.Provider;
import com.example.SecureAndBox.login.dto.request.LoginDto;
import com.example.SecureAndBox.login.dto.request.LoginRequestDto;
import com.example.SecureAndBox.login.dto.request.SignUpDto;
import com.example.SecureAndBox.login.dto.response.JwtTokenResponse;
import com.example.SecureAndBox.login.exception.CustomException;
import com.example.SecureAndBox.oauth.dto.KakaoTokenResponse;
import com.example.SecureAndBox.oauth.security.info.UserAuthentication;

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

	private final UserService userService;

	private final PasswordEncoder passwordEncoder;

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

		logger.log(Level.INFO, "Kakao login redirect requested");
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
	public ResponseEntity<?> login(
		@Valid @RequestBody LoginDto dto
	)
	{
		String id = dto.getUsername();
		String pw = dto.getPw();
		// 유효성 검사

		if (!isValidId(id) || !isValidPassword(pw)) {
			return ResponseEntity.badRequest().body("Invalid ID or Password format");
		}

		// 로그인 처리
		return ResponseEntity.ok(authService.notSocialLogin(id, pw));
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
		if(Pattern.matches(idPattern, id))
			return true;
		else
			throw new CustomException(INVALID_USER_ID);
	}

	private boolean isValidPassword(String pw) {
		// 비밀번호 유효성 검사 로직 (예: 영숫자와 특수문자만 허용, 8~30자 사이)
		String passwordPattern = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,30}$";
		if(Pattern.matches(passwordPattern, pw))
			return true;
		else
			throw new CustomException(INVALID_PASSWORD);
	}


	/*@Operation(summary = "카카오 로그인 토큰 받아오기 -> 인가코드 주입하고 토큰 받기")
	@GetMapping("/callback")
	public ResponseEntity<?> kakaoCallback(@RequestParam String code) throws IOException {
		try {
			System.out.println("callback\n\n\n");
			KakaoTokenResponse accessToken = kakaoLoginService.getAccessToken(code, apiKey, redirectUri);
			LoginRequestDto request = new LoginRequestDto(Provider.KAKAO, null); // Name can be null here
			JwtTokenResponseDto tokens = authService.login(accessToken, request);
			return ResponseEntity.ok(tokens);
		} catch (AuthenticationException e) {
			// Handle general authentication errors
			logger.log(Level.WARNING, "Authentication failed: " + e.getMessage(), e);
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Authentication failed.");
		} catch (IOException e) {
			// Handle IO-related errors
			logger.log(Level.SEVERE, "IO error during Kakao callback processing: " + e.getMessage(), e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal server error.");
		} catch (Exception e) {
			// Catch-all for any other exceptions
			logger.log(Level.SEVERE, "Unexpected error during Kakao callback processing: " + e.getMessage(), e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred.");
		}
	}*/
	@Operation(summary = "카카오 로그인 토큰 받아오기 -> 인가코드 주입하고 토큰 받기")
	@GetMapping("/callback")
	public ResponseEntity<String> kakaoCallback(@RequestParam String code) {
		try {
			// 카카오 API를 사용하여 액세스 토큰 및 리프레시 토큰 가져오기
			KakaoTokenResponse accessToken = kakaoLoginService.getAccessToken(code, apiKey, redirectUri);
			LoginRequestDto request = new LoginRequestDto(Provider.KAKAO, null); // Name은 여기서 null로 설정

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
               
            </script>
            """, tokens.getAccessToken(), tokens.getRefreshToken());

			return ResponseEntity.ok()
					.contentType(MediaType.TEXT_HTML)
					.body(htmlContent);

		} catch (AuthenticationException e) {
			logger.log(Level.WARNING, "Authentication failed: " + e.getMessage(), e);
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("<script>alert('Authentication failed.'); window.close();</script>");
		} catch (IOException e) {
			logger.log(Level.SEVERE, "IO error during Kakao callback processing: " + e.getMessage(), e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("<script>alert('Internal server error.'); window.close();</script>");
		} catch (Exception e) {
			logger.log(Level.SEVERE, "Unexpected error during Kakao callback processing: " + e.getMessage(), e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("<script>alert('An unexpected error occurred.'); window.close();</script>");
		}
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
		JwtTokenResponseDto tokens = authService.login(response, request);
		return ResponseEntity.ok((tokens));
	}

}

package com.example.SecureAndBox.login.application;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.example.SecureAndBox.login.api.KakaoFeignClient;
import com.example.SecureAndBox.login.dto.SocialInfoDto;
import com.example.SecureAndBox.oauth.dto.KakaoTokenResponse;
import com.example.SecureAndBox.oauth.dto.KakaoUserDto;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class KakaoLoginService {
	private final KakaoFeignClient kakaoFeignClient;

	private final RestTemplate restTemplate;

	@Value("${kakao.api.key}")
	private String apiKey;
	@Value("${kakao.redirect.url}")
	private String redirectUri;// Replace with your actual redirect URI

	private String tokenUri = "https://kauth.kakao.com/oauth/token";

	public SocialInfoDto getInfo(String providerToken) {
		KakaoUserDto kakaoUserdto = kakaoFeignClient.getUserInformation("Bearer " + providerToken);
		return SocialInfoDto.of(
			kakaoUserdto.id().toString(),
			"test",
			kakaoUserdto.kakaoAccount().kakaoUserProfile().nickname());
	}

	public KakaoTokenResponse getAccessToken(String code, String clientId, String redirectUri) {

		UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(tokenUri)
			.queryParam("grant_type", "authorization_code")
			.queryParam("client_id", clientId)
			.queryParam("redirect_uri", redirectUri)
			.queryParam("code", code);

		HttpHeaders headers = new HttpHeaders();
		headers.set("Content-Type", "application/x-www-form-urlencoded");

		HttpEntity<String> entity = new HttpEntity<>(headers);

		try {
			ResponseEntity<KakaoTokenResponse> response = restTemplate.exchange(
				uriBuilder.toUriString(),
				HttpMethod.POST,
				entity,
				KakaoTokenResponse.class
			);

			KakaoTokenResponse responseBody = response.getBody();
			if (responseBody != null) {
				return responseBody;
			} else {
				throw new RuntimeException("Failed to get access token from Kakao: " + responseBody);
			}
		} catch (HttpClientErrorException e) {
			throw new RuntimeException("HTTP error while getting access token from Kakao: " + e.getStatusCode() + " - "
				+ e.getResponseBodyAsString(), e);
		} catch (Exception e) {
			throw new RuntimeException("Unexpected error while getting access token from Kakao", e);
		}
	}

	public KakaoTokenResponse refreshKakaoToken(String refreshToken) {

		UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(tokenUri)
			.queryParam("grant_type", "refresh_token")
			.queryParam("client_id", apiKey)
			.queryParam("refresh_token", refreshToken);

		HttpHeaders headers = new HttpHeaders();
		headers.set("Content-Type", "application/x-www-form-urlencoded");

		HttpEntity<String> entity = new HttpEntity<>(headers);

		try {
			ResponseEntity<KakaoTokenResponse> response = restTemplate.exchange(
				uriBuilder.toUriString(),
				HttpMethod.POST,
				entity,
				KakaoTokenResponse.class
			);

			KakaoTokenResponse responseBody = response.getBody();
			if (responseBody != null) {
				return responseBody;
			} else {
				throw new RuntimeException("Failed to get access token from Kakao: " + responseBody);
			}
		} catch (HttpClientErrorException e) {
			throw new RuntimeException("HTTP error while getting access token from Kakao: " + e.getStatusCode() + " - "
				+ e.getResponseBodyAsString(), e);
		} catch (Exception e) {
			throw new RuntimeException("Unexpected error while getting access token from Kakao", e);
		}
	}

}
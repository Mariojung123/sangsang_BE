package com.example.SecureAndBox.login.api;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpHeaders;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import com.example.SecureAndBox.oauth.dto.KakaoTokenResponse;
import com.example.SecureAndBox.oauth.dto.KakaoUserDto;

@FeignClient(name = "kakaoFeignClient", url = "https://kapi.kakao.com")
public interface KakaoFeignClient {
	@GetMapping(value = "/v2/user/me")
	KakaoUserDto getUserInformation(@RequestHeader(HttpHeaders.AUTHORIZATION) String accessToken);

	@PostMapping(value = "/oauth/token", consumes = "application/x-www-form-urlencoded")
	KakaoTokenResponse refreshToken(@RequestBody MultiValueMap<String, String> paramMap);
}
package com.example.SecureAndBox.login.domain;

public class Constants {
	public static final String USER_ID_CLAIM_NAME = "uid";
	public static final String USER_ROLE_CLAIM_NAME = "rol";
	public static final String BEARER_PREFIX = "Bearer ";
	public static final String AUTHORIZATION_HEADER = "Authorization";
	public static final String PROVIDER_TOKEN_HEADER = "X-Provider-Token";
	public static final String[] AUTH_WHITELIST = {
		//"/api/**",
		"/api/oauth",
		"/api/oauth/refresh-kakao-token",
		"/api/oauth/logout",
		"/api/oauth/callback",
		"/api/oauth/refresh",
		"/actuator/health",
		"/api-docs.html",
		"/api-docs/**",
		"/swagger-ui/**",
		"/swagger-ui.html",
	};

}
package com.example.SecureAndBox.oauth.security.filter;

import java.io.IOException;

import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;


import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.example.SecureAndBox.login.domain.Constants;
import com.example.SecureAndBox.oauth.security.info.UserAuthentication;
import com.example.SecureAndBox.oauth.utils.JwtUtil;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	private final JwtUtil jwtUtil;

	@Override
	protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response,
		@NonNull FilterChain filterChain) throws ServletException, IOException {

		final String token = getJwtFromRequest(request);

		if (StringUtils.hasText(token)) {
			Claims claims = jwtUtil.getTokenBody(token);
			Long userId = claims.get(Constants.USER_ID_CLAIM_NAME, Long.class);
			if (claims.get(Constants.USER_ROLE_CLAIM_NAME, String.class) == null) {
				System.out.println(request.getRequestURI());
				if (!request.getRequestURI().equals("/api/oauth/refresh"))
					throw new AuthenticationServiceException("User role claim is missing in the token.");
			}
			UserAuthentication authentication = new UserAuthentication(userId, null, null, token);
			authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
			SecurityContextHolder.getContext().setAuthentication(authentication);
		}

		filterChain.doFilter(request, response);
	}

	private String getJwtFromRequest(HttpServletRequest request) {
		String bearerToken = request.getHeader(Constants.AUTHORIZATION_HEADER);
		if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(Constants.BEARER_PREFIX)) {
			return bearerToken.substring(Constants.BEARER_PREFIX.length());
		}
		return null;
	}
}
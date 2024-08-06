package com.example.SecureAndBox.oauth.security.filter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class CustomJwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
	private final ObjectMapper objectMapper = new ObjectMapper();


	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
		AuthenticationException authException) throws IOException {
		setResponse(response, authException);
	}

	private void setResponse(HttpServletResponse response, AuthenticationException authException) throws IOException {
		response.setCharacterEncoding("UTF-8");
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

		// Create a JSON response body with a detailed error message
		Map<String, Object> responseBody = new HashMap<>();
		responseBody.put("error", "Unauthorized");
		responseBody.put("message", authException.getMessage());
		responseBody.put("timestamp", System.currentTimeMillis());
		responseBody.put("status", HttpServletResponse.SC_UNAUTHORIZED);

		// Write the JSON response
		response.getWriter().println(objectMapper.writeValueAsString(responseBody));
	}

}
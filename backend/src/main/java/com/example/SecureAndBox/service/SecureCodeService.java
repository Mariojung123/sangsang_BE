package com.example.SecureAndBox.service;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

import com.example.SecureAndBox.dto.CodeSubmission;
import com.example.SecureAndBox.entity.Problem;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SecureCodeService {
	private final ProblemService problemService;
	private static final String PROBLEM_SERVER_URL = "http://localhost:8080/api/problem/verify";

	public CompletableFuture<String> verifyAndForwardCode(CodeSubmission submission) {
		return CompletableFuture.supplyAsync(() -> {
			try {
				// Encode the user code using Base64
				String encodedUserCode = Base64.getEncoder().encodeToString(submission.getUserCode().getBytes(StandardCharsets.UTF_8));

				// Update the submission with the encoded code
				submission.setUserCode(encodedUserCode);

				// Serialize to JSON
				ObjectMapper objectMapper = new ObjectMapper();
				String jsonPayload = objectMapper.writeValueAsString(submission);

				// Send the request
				HttpClient client = HttpClient.newHttpClient();
				HttpRequest request = HttpRequest.newBuilder()
					.uri(URI.create(PROBLEM_SERVER_URL))
					.header("Content-Type", "application/json")
					.POST(HttpRequest.BodyPublishers.ofString(jsonPayload, StandardCharsets.UTF_8))
					.build();

				return client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
					.thenApply(HttpResponse::body)
					.join();

			} catch (Exception e) {
				throw new RuntimeException("Failed to forward code to problem server.", e);
			}
		});
	}

	public CompletableFuture<String> forwardToProblemServer(CodeSubmission submission) {
		return CompletableFuture.supplyAsync(() -> {
			try {
				HttpClient client = HttpClient.newHttpClient();

				// Serialize the CodeSubmission object to JSON
				ObjectMapper objectMapper = new ObjectMapper();
				String jsonPayload = objectMapper.writeValueAsString(submission);

				// Build the POST request with JSON payload
				HttpRequest request = HttpRequest.newBuilder()
					.uri(URI.create(PROBLEM_SERVER_URL))
					.header("Content-Type", "application/json")
					.POST(HttpRequest.BodyPublishers.ofString(jsonPayload, StandardCharsets.UTF_8))
					.build();

				// Send the request asynchronously and get the response
				return client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
					.thenApply(HttpResponse::body) // Process the whole response body
					.join(); // Block to get the result for demonstration purposes

			} catch (Exception e) {
				throw new RuntimeException("Failed to forward code to problem server.", e);
			}
		});
	}

	private String processServerResponse(String response) {
		// Process the response from the problem server and return a meaningful message
		return "Server Response: " + response;
	}


}
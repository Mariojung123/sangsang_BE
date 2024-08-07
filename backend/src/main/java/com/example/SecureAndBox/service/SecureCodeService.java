package com.example.SecureAndBox.service;
import org.springframework.beans.factory.annotation.Value;
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
import java.util.concurrent.CompletionException;
import java.util.concurrent.CompletionStage;

import com.example.SecureAndBox.dto.CodeSubmission;
import com.example.SecureAndBox.entity.Problem;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
@Service
@RequiredArgsConstructor
public class SecureCodeService {
	private final ProblemService problemService;

	@Value("${code.host}")
	private String PROBLEM_SERVER_URL;


	private final ObjectMapper objectMapper;

	private final HttpClient httpClient = HttpClient.newHttpClient();

	public CompletableFuture<String> verifyAndForwardCode(CodeSubmission submission) {
		return CompletableFuture.supplyAsync(() -> {
			try {
				// Encode user code to Base64
			//	String encodedUserCode = Base64.getEncoder()
				//	.encodeToString(submission.getUserCode().getBytes(StandardCharsets.UTF_8));
			//	submission.setUserCode(encodedUserCode);

				// Convert CodeSubmission object to JSON
				String jsonPayload = objectMapper.writeValueAsString(submission.getUserCode());

				// Create an HTTP request
				HttpRequest request = HttpRequest.newBuilder()
					.uri(URI.create(PROBLEM_SERVER_URL))
					.header("Content-Type", "application/json")
					.POST(HttpRequest.BodyPublishers.ofString(jsonPayload))
					.build();

				// Send the request asynchronously
				return httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString())
					.thenApply(HttpResponse::body)
					.exceptionally(ex -> {
						// Throw a CompletionException with a message and cause
						throw new CompletionException("Error during HTTP request", ex);
					})
					.join();
			} catch (Exception e) {
				// Handle exceptions that occur outside of CompletableFuture
				throw new CompletionException("Error processing code submission", e);
			}
		});
	}
}




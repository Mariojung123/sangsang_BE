package com.example.SecureAndBox.service;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.stereotype.Service;


import java.net.URI;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;


import com.example.SecureAndBox.dto.CodeSubmission;

import com.fasterxml.jackson.databind.JsonNode;
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
				CodePayload payload = new CodePayload(submission.getUserCode());
				String jsonPayload = objectMapper.writeValueAsString(payload);

				// Create an HTTP request
				HttpRequest request = HttpRequest.newBuilder()
					.uri(URI.create(PROBLEM_SERVER_URL))
					.header("Content-Type", "application/json")
					.POST(HttpRequest.BodyPublishers.ofString(jsonPayload))
					.build();

				// Send the request asynchronously
				return httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString())
					.thenApply(response -> handleServerResponse(response.body()))
					.exceptionally(ex -> {
						throw new CompletionException("Error during HTTP request", ex);
					})
					.join();
			} catch (Exception e) {
				throw new CompletionException("Error processing code submission", e);
			}
		});
	}

	static class CodePayload {
		private final String input;

		public CodePayload(String input) {
			this.input = input;
		}

		public String getInput() {
			return input;
		}
	}

	// Handle server response
	private String handleServerResponse(String responseBody) {
		try {
			if (responseBody.contains("works\nhacked\n")) {
				return "Success: Code executed without errors.";
			} else {
				// Attempt to parse JSON response
				JsonNode jsonNode = objectMapper.readTree(responseBody);

				// Check if the response contains a message of invalid code
				if (jsonNode.has("message") && jsonNode.get("message").asText().contains("Invalid code")) {
					String output = jsonNode.get("output").asText();
					return "Error: Invalid code detected. Details: " + output;
				} else if (jsonNode.has("error")) {
					String errorDetails = jsonNode.get("error").asText();
					return "Error: " + errorDetails;
				}
			}
			return "Unexpected response format.";
		} catch (Exception e) {
			return "Error processing server response: " + e.getMessage();
		}
	}
}




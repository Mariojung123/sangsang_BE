package com.example.SecureAndBox.service;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.stereotype.Service;


import java.net.URI;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.logging.Logger;

import com.example.SecureAndBox.dto.CodeSubmission;

import com.example.SecureAndBox.entity.Problem;
import com.example.SecureAndBox.entity.User;
import com.example.SecureAndBox.entity.UserProblemRelation;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.io.IOException;
import lombok.RequiredArgsConstructor;
@Service
@RequiredArgsConstructor
public class SecureCodeService {
	private final ProblemService problemService;

	private final UserProblemService userProblemService;

	@Value("${code.host}")
	private String PROBLEM_SERVER_URL;


	private final ObjectMapper objectMapper;
	private static final Logger logger = Logger.getLogger(ProblemService.class.getName());
	private final HttpClient httpClient = HttpClient.newHttpClient();

	public CompletableFuture<String> verifyAndForwardCode(CodeSubmission submission, User user) {
		return CompletableFuture.supplyAsync(() -> {
			try {

				Problem problem = problemService.getProblemById(submission.getProblemId());
				UserProblemRelation up = userProblemService.createUserProblem(user,problem);


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
					.thenApply(response -> {
						try {
							return handleServerResponse(response.body(),up);
						} catch (JsonProcessingException e) {
							throw new RuntimeException(e);
						}
					})
					.exceptionally(ex -> {
						throw new CompletionException("Error during HTTP request", ex);
					})
					.join();
			} catch (JsonProcessingException e) {
				// Handle specific JSON processing errors
				throw new CompletionException("Error serializing the code submission to JSON", e);
			} catch (IOException e) {
				// Handle I/O related errors (e.g., network issues)
				throw new CompletionException("I/O error occurred while processing the code submission", e);
			} catch (Exception e) {
				// Fallback for any other unforeseen exceptions
				throw new CompletionException("Unexpected error during code submission processing", e);
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
	private String handleServerResponse(String responseBody, UserProblemRelation up) throws JsonProcessingException {
		JsonNode jsonNode = objectMapper.readTree(responseBody);
		String result;

		try {
			if (jsonNode.has("message")) {
				String message = jsonNode.get("message").asText();

				if (message.contains("hacked")) {
					result = responseBody;
				} else if (message.contains("you protected")) {
					userProblemService.saveRelation(up);

					result = responseBody;
				} else if (message.contains("Invalid code") && jsonNode.has("output")) {
					String output = jsonNode.get("output").asText();
					result = "Error: Invalid code detected. Details: " + output;
				} else if (message.contains("Exploit execution failed")) {
					result = responseBody;
				} else {
					result = "Unexpected response format.";
				}
			} else if (jsonNode.has("error")) {
				String errorDetails = jsonNode.get("error").asText();
				result = "Error: " + errorDetails;
			} else {
				result = "Unexpected response format.";
			}
		} catch (Exception e) {
			throw new RuntimeException("Error processing server response: " + e.getMessage(), e);
		}

		return result;
	}


}




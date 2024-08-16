package com.example.SecureAndBox.service;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.stereotype.Service;


import java.net.URI;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.example.SecureAndBox.dto.CodeSubmission;

import com.example.SecureAndBox.entity.Problem;
import com.example.SecureAndBox.entity.User;
import com.example.SecureAndBox.entity.UserProblemRelation;
import com.example.SecureAndBox.login.api.AuthController;
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

	private final HttpClient httpClient = HttpClient.newHttpClient();

	private static final Logger logger = Logger.getLogger(SecureCodeService.class.getName());

	public CompletableFuture<String> verifyAndForwardCode(CodeSubmission submission, User user) {
		return CompletableFuture.supplyAsync(() -> {
			try {
				Problem problem = problemService.getProblemById(submission.getProblemId());
				UserProblemRelation up = userProblemService.createUserProblem(user,problem);
				logger.log(Level.INFO, "User :  "+user.getUsername() +"problem"  + problem.getTitle());

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
						return handleServerResponse(response.body(),up);
					})
					.exceptionally(ex -> {
						throw new CompletionException("Error during HTTP request", ex);
					})
					.join();
				//부적절한 예외처리
			} catch (JsonProcessingException e) {
				logger.log(Level.SEVERE, "Error processing code submission payload", e);
				throw new CompletionException("Error processing code submission payload", e);
			} catch (IOException e) {
				logger.log(Level.SEVERE, "I/O error occurred during processing", e);
				throw new CompletionException("I/O error occurred during processing", e);
			} catch (Exception e) {
				logger.log(Level.SEVERE, "Unexpected error occurred during code submission processing", e);
				throw new CompletionException("Unexpected error occurred during code submission processing", e);
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
	private String handleServerResponse(String responseBody, UserProblemRelation up) {
		String result;

		try {
			JsonNode jsonNode = objectMapper.readTree(responseBody);

			if (jsonNode.has("message") && jsonNode.has("output") && jsonNode.has("url")) {
				String output = jsonNode.get("output").asText();
				String url = jsonNode.get("url").asText();
				if (output != null && url != null && output.contains("hacked")) {
					result = output + "\n" + url;
				} else if (output != null && url != null && output.contains("you protected")) {
					userProblemService.saveRelation(up);
					result = output + "\n" + url;
				} else {
					result = "Unexpected response format.";
				}
			} else if (jsonNode.has("error")) {
				if (jsonNode.has("message") && jsonNode.get("message") != null) {
					String message = jsonNode.get("message").asText();
					if (message.equals("Incorrect syntax in function")) {
						result = message;
					} else if (message.equals("Invalid code")) {
						String output = jsonNode.has("output") ? jsonNode.get("output").asText() : "No output";
						result = message;
					} else {
						result = responseBody + "\n\nError: " + message;
					}
				} else {
					String errorDetails = jsonNode.get("error") != null ? jsonNode.get("error").asText() : "Unknown error";
					result = responseBody + "\n\nError: " + errorDetails;
				}
			} else {
				result = "Unexpected response format.";
			}
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			result = "Error processing response: " + e.getMessage();
		}

		return result;
	}

}




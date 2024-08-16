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
	private String handleServerResponse(String responseBody, UserProblemRelation up) throws JsonProcessingException {
		System.out.println("Server response: " + responseBody);

		try {
			JsonNode jsonNode = objectMapper.readTree(responseBody);
			System.out.println("jsonNode: " + jsonNode);

			if (jsonNode.has("message") && jsonNode.has("output")) {
				String output = jsonNode.get("output").asText();
				if (output != null && output.contains("hacked")) {
					return responseBody;
				} else if (output != null && output.contains("you protected")) {
					userProblemService.saveRelation(up);
					return responseBody;
				}
			}

			if (jsonNode.has("error")) {
				if (jsonNode.has("message") && jsonNode.get("message") != null) {
					String message = jsonNode.get("message").asText();
					System.out.println("message: " + message);
					if (message.equals("Incorrect syntax in function")) {
						return responseBody;
					} else if (message.equals("Invalid code")) {
						String output = jsonNode.has("output") ? jsonNode.get("output").asText() : "No output";
						return responseBody;
					}
				}
				String errorDetails = jsonNode.get("error") != null ? jsonNode.get("error").asText() : "Unknown error";
				return responseBody + "\n\nError: " + errorDetails;
			}

			return "Unexpected response format.";
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			return "Error processing response: " + e.getMessage();
		}
	}
}




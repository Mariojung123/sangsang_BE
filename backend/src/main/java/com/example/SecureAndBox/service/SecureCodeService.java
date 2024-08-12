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

import com.example.SecureAndBox.entity.Problem;
import com.example.SecureAndBox.entity.User;
import com.example.SecureAndBox.entity.UserProblemRelation;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

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
	private String handleServerResponse(String responseBody, UserProblemRelation up) throws JsonProcessingException {
		JsonNode jsonNode = objectMapper.readTree(responseBody);
		System.out.println(responseBody);
		try {
			if (jsonNode.has("message")) {
				String message = jsonNode.get("message").asText();

				if (message.contains("hacked")) {
					System.out.println("hacked");
					return responseBody;
				} else if (message.contains("you protected")) {
					userProblemService.saveRelation(up);
					System.out.println("you protected");
					return responseBody;
				} else if (message.contains("Invalid code") && jsonNode.has("output")) {
					String output = jsonNode.get("output").asText();
					return "Error: Invalid code detected. Details: " + output;
				}
			}

			if (jsonNode.has("error")) {
				String errorDetails = jsonNode.get("error").asText();
				return "Error: " + errorDetails;
			}

			return "Unexpected response format.";
		} catch (Exception e) {
			return "Error processing server response: " + e.getMessage();
		}
	}

}




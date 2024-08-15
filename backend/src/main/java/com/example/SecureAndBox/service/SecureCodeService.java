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
	private String handleServerResponse(String responseBody,UserProblemRelation up) throws JsonProcessingException {
		JsonNode jsonNode = objectMapper.readTree(responseBody);
		try {
			if (jsonNode.has("message") && jsonNode.get("output").asText().contains("hacked")) {
				return responseBody;
			} else if(jsonNode.has("message") && jsonNode.get("output").asText().contains("you protected")) {
				userProblemService.saveRelation(up);
				return responseBody;
			}else if(jsonNode.get("message").asText().contains("Incorrect syntax in function")) {
				return "기능 구현에 실패하였습니다.";
			}
			else {
				if (jsonNode.has("message") && jsonNode.get("message").asText().contains("Invalid code")) {
					String output = jsonNode.get("output").asText();
					return "빌드에 실패하였습니다. 코드를 다시 확인해주세요. Details: " + output;
				}
				else if (jsonNode.has("error") || jsonNode.get("message").asText().contains("Incorrect syntax in function")) {
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




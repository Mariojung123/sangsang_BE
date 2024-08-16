package com.example.SecureAndBox.controller;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.*;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.AccessDeniedException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.concurrent.CompletableFuture;

import com.example.SecureAndBox.dto.CodeSubmission;
import com.example.SecureAndBox.dto.ProblemRequestDto;
import com.example.SecureAndBox.entity.Problem;
import com.example.SecureAndBox.entity.User;
import com.example.SecureAndBox.etc.LanguageType;
import com.example.SecureAndBox.login.interceptor.UserId;
import com.example.SecureAndBox.service.ProblemService;
import com.example.SecureAndBox.service.SecureCodeService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/problem")
@RequiredArgsConstructor
public class ProblemController {

	private final SecureCodeService secureCodeService;

	private final ProblemService problemService;

	@Async
	@PostMapping("/submit")
	public CompletableFuture<ResponseEntity<String>> handleFileUpload(
		@Parameter(hidden = true) @UserId User user,
	    @RequestBody CodeSubmission submission) {


		return secureCodeService.verifyAndForwardCode(submission,user)
			.thenApply(response -> ResponseEntity.ok(response))
			.exceptionally(ex -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
				.body("An error occurred: " + ex.getMessage()));
	}

	@Operation(summary = "문제 제목 가져오기")
	@GetMapping("")
	public ResponseEntity<?> getProblemList(
		@RequestParam(defaultValue = "0") int page,
		@RequestParam(defaultValue = "10") int size
	) {
		Pageable pageable = PageRequest.of(page, size);

		return ResponseEntity.ok(problemService.getProblemList(pageable));
	}
	@Operation(summary="스켈레톤 코드 가져오기")
	@GetMapping("/skeleton-code")
	public ResponseEntity<String> getSkeletonCode(
		@RequestParam("pid") Long pid,
		@RequestParam LanguageType type) {
		Problem problem = problemService.getProblemById(pid);
		try {
			String content = problemService.getSkeletonCode(problem.getType(),type.getKey());
			return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_TYPE, "text/plain;charset=UTF-8")
				.body(content);
		} catch (IOException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("File not found or read error");
		}
	}
	@Operation(summary = "문제 디테일 가져오기")
	@GetMapping("/details")
	public ResponseEntity<?> getProblemDetails(
		@RequestParam Long pid) throws IOException {
		return ResponseEntity.ok(problemService.getProblem(pid));
	}
	@Operation(summary = "문제 생성")
	@PostMapping("")
	public ResponseEntity<?> createProblem(
		@Parameter(hidden = true) @UserId User user,
		@RequestBody ProblemRequestDto dto) throws AccessDeniedException {
		problemService.createProblem(user,dto);
		return ResponseEntity.ok("문제가 생성되었습니다.");
	}
}
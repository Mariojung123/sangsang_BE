package com.example.SecureAndBox.controller;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.example.SecureAndBox.dto.CodeSubmission;
import com.example.SecureAndBox.etc.LanguageType;
import com.example.SecureAndBox.service.ProblemService;
import com.example.SecureAndBox.service.SecureCodeService;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/problem")
@RequiredArgsConstructor
public class ProblemController {

	private final SecureCodeService secureCodeService;

	private final ProblemService problemService;


	@PostMapping("/submit")
	public ResponseEntity<String> handleFileUpload(@RequestBody CodeSubmission submission) {
		//코드에 악의적인 코드가 없는지 검증하는 코드 구현 이후 파일로 저장
		Path tempDir = Paths.get(System.getProperty("java.io.tmpdir"));
		Path codePath = tempDir.resolve("UserCode.java");
		try {
			// 파일 저장
			Files.write(codePath, submission.getUserCode().getBytes());

			// 시큐어 코딩 테스트 실행
			String result = secureCodeService.testSecureCode(codePath, submission);
			return ResponseEntity.ok(result);
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
			return ResponseEntity.status(500).body("Server error occurred.");
		} finally {
			// 임시 파일 삭제
			try {
				Files.deleteIfExists(codePath);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@GetMapping("")
	public ResponseEntity<?> getProblemList(
		@RequestParam(defaultValue = "0") int page,
		@RequestParam(defaultValue = "10") int size
	) {
		Pageable pageable = PageRequest.of(page, size);

		return ResponseEntity.ok(problemService.getProblemList(pageable));
	}

	@GetMapping("/topic")
	public ResponseEntity<?> getProblemListByTopic(@RequestParam(defaultValue="login") String topic)
	 {
		return ResponseEntity.ok(problemService.getProblemListByTopic(topic));
	}

	@GetMapping("/difficulty")
	public ResponseEntity<?> getProblemListByDfficulty(@RequestParam(defaultValue="EASY") String difficulty)
	{
		return ResponseEntity.ok(problemService.getProblemListByDifficulty(difficulty));
	}
	@Operation(summary="스켈레톤 코드 가져오기")
	@GetMapping("/skeleton-code")
	public ResponseEntity<String> getSkeletonCode(@RequestParam("filename") String filename,@RequestParam LanguageType type) {
		try {
			String content = problemService.getSkeletonCode(filename,type);
			return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_TYPE, "text/plain;charset=UTF-8")
				.body(content);
		} catch (IOException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("File not found or read error");
		}
	}
	@GetMapping("/details")
	public ResponseEntity<?> getProblemDetails(
		@RequestParam Long problemId,
		@RequestParam(defaultValue = "JAVA") LanguageType type) throws IOException {
		return ResponseEntity.ok(problemService.getProblem(problemId,type));
	}
}
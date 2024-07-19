package com.example.KEBProject.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.example.KEBProject.dto.CodeSubmission;
import com.example.KEBProject.service.SecureCodeService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/code")
@RequiredArgsConstructor
public class CodeController {

	private final SecureCodeService secureCodeService;


	@PostMapping("/upload")
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
}
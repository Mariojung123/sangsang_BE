package com.example.SecureAndBox.controller;

import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.SecureAndBox.dto.CommentRequestDto;
import com.example.SecureAndBox.entity.User;
import com.example.SecureAndBox.login.interceptor.UserId;
import com.example.SecureAndBox.service.CommentService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/comment")
@RequiredArgsConstructor
public class CommentController {
	private final CommentService commentService;

	@Operation(summary = "댓글 생성")
	@PostMapping("/create")
	public ResponseEntity<?> createComment(
		@Parameter(hidden = true) @UserId User user,
		@Valid @RequestBody CommentRequestDto commentDto) {

		String description = Jsoup.clean(commentDto.getContent(), Safelist.basic()); // XSS 방지

		ResponseEntity<?> responseEntity;
		try {
			commentService.createComment(commentDto, description, user);
			responseEntity = ResponseEntity.ok().build();

		} catch (IllegalArgumentException e) {
			// 잘못된 인자에 대한 처리
			responseEntity = ResponseEntity.status(HttpStatus.BAD_REQUEST).body("잘못된 요청입니다. " + e.getMessage());

		} catch (DataIntegrityViolationException e) {
			// 데이터 무결성 위반에 대한 처리
			responseEntity = ResponseEntity.status(HttpStatus.CONFLICT).body("데이터 무결성 오류가 발생했습니다.");

		} catch (Exception e) {
			// 그 외 예외 처리
			responseEntity = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("서버 오류가 발생했습니다.");
		}
		return responseEntity;
	}

	@Operation(summary = "댓글 삭제")
	@DeleteMapping("/delete")
	public ResponseEntity<?> deleteComment(
		@Parameter(hidden = true) @UserId User user,
		@RequestParam Long commentId) {

		ResponseEntity<?> responseEntity;
		try {
			commentService.deleteComment(commentId, user);
			responseEntity = ResponseEntity.ok().build();

		} catch (IllegalArgumentException e) {
			// 잘못된 인자에 대한 처리
			responseEntity = ResponseEntity.status(HttpStatus.BAD_REQUEST).body("잘못된 요청입니다. " + e.getMessage());

		} catch (EntityNotFoundException e) {
			// 댓글을 찾을 수 없는 경우에 대한 처리
			responseEntity = ResponseEntity.status(HttpStatus.NOT_FOUND).body("댓글을 찾을 수 없습니다.");

		} catch (Exception e) {
			// 그 외 예외 처리
			responseEntity = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("서버 오류가 발생했습니다.");
		}
		return responseEntity;
	}

	@Operation(summary = "댓글 조회")
	@GetMapping("")
	public ResponseEntity<?> getComments(
		@Parameter(hidden = true) @UserId User user,
		@RequestParam("postId") Long postId) {
		return ResponseEntity.ok(commentService.getComments(postId, user));
	}


}

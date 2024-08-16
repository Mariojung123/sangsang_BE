package com.example.SecureAndBox.controller;

import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.SecureAndBox.dto.CommentRequestDto;
import com.example.SecureAndBox.dto.PostRequestDto;
import com.example.SecureAndBox.entity.User;
import com.example.SecureAndBox.login.interceptor.UserId;
import com.example.SecureAndBox.service.CommentService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/comment")
@RequiredArgsConstructor
public class CommentController {
	private final CommentService commentService;

	@Operation(summary = "댓글 생성")
	@PostMapping("/create")
	public ResponseEntity<?> createPost(
		@Parameter(hidden = true) @UserId User user,
		@RequestBody CommentRequestDto commentDto) {
		String description = Jsoup.clean(commentDto.getContent(), Safelist.basic());



		commentService.createComment(commentDto,description, user);

		return ResponseEntity.ok().build();
	}

	@Operation(summary = "댓글 삭제")
	@PostMapping("/delete")
	public ResponseEntity<?> deletePost(
		@Parameter(hidden = true) @UserId User user,
		@RequestParam Long commentId) {
		commentService.deleteComment(commentId, user);
		return ResponseEntity.ok().build();
	}

	@Operation(summary = "댓글 조회")
	@GetMapping("")
	public ResponseEntity<?> getComments(
		@Parameter(hidden = true) @UserId User user,
		@RequestParam("postId") Long postId) {
		return ResponseEntity.ok(commentService.getComments(postId, user));
	}


}

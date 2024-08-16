package com.example.SecureAndBox.controller;

import org.jsoup.safety.Safelist;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.SecureAndBox.dto.PostRequestDto;
import com.example.SecureAndBox.entity.Post;
import com.example.SecureAndBox.entity.User;
import com.example.SecureAndBox.login.interceptor.UserId;
import com.example.SecureAndBox.service.CommentService;
import com.example.SecureAndBox.service.PostService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.jsoup.Jsoup;

@RestController
@RequestMapping("/api/post")
@RequiredArgsConstructor
public class PostController {
	private final PostService postService;
	private final CommentService commentService;

	@Operation(summary = "게시물 생성")
	@PostMapping("/create")
	public ResponseEntity<?> createPost(
		@Parameter(hidden = true) @UserId User user,
		@RequestBody PostRequestDto postDto) {
		String description = Jsoup.clean(postDto.getContent(), Safelist.basic());
		postService.createPost(postDto,description, user);

		return ResponseEntity.ok().build();
	}

	@Operation(summary = "게시물 작성 시 parent 불러오기")
	@GetMapping("/parent")
	public ResponseEntity<?> getParentPost() {
		return ResponseEntity.ok(postService.getParentPost());
	}


	@Operation(summary = "게시물 삭제")
	@DeleteMapping("/delete")
	public ResponseEntity<?> deletePost(@Parameter(hidden = true) @UserId User user,
		@RequestParam Long postId){


		postService.deletePost(postId, user);
		return ResponseEntity.ok().build();
	}

	@Operation(summary = "게시물 상세보기")
	@GetMapping("/read")
	public ResponseEntity<?> readPost(@Parameter(hidden = true) @UserId User user,
		@RequestParam Long postId){


		return ResponseEntity.ok(postService.getpostDetials(postId,user));
	}
	@Operation(summary = "게시물 보기")
	@GetMapping("")
	public ResponseEntity<?> getPostList(@Parameter(hidden = true) @UserId User user){


		return ResponseEntity.ok(postService.getpost(user));
	}

	@Operation(summary = "문제에 해당하는 게시물 보기")
	@GetMapping("/read/parent")
	public ResponseEntity<?> getPostListByParent(@Parameter(hidden = true) @UserId User user,
		@RequestParam Long parent){


		return ResponseEntity.ok(postService.getpostByParent(user,parent));
	}



}

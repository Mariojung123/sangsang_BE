package com.example.SecureAndBox.controller;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.*;
import org.webjars.NotFoundException;

import com.example.SecureAndBox.dto.VideoRequestDto;
import com.example.SecureAndBox.entity.Video;
import com.example.SecureAndBox.service.VideoService;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.persistence.EntityExistsException;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/video")
@RequiredArgsConstructor
public class VideoController {

	private final VideoService videoService;

	@GetMapping("")
	public ResponseEntity<?> getVideoList(
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size
	) {
		Pageable pageable = PageRequest.of(page, size);
		return ResponseEntity.ok(videoService.getList(pageable));
	}

	@GetMapping("/details")
	public ResponseEntity<?> getVideo(@RequestParam Long videoId) {
		return ResponseEntity.ok(videoService.getVideo(videoId));
	}

	@Operation(summary = "영상 추가 -> 어드민만 가능")
	@PostMapping("")
	public ResponseEntity<String> createVideo(@RequestBody VideoRequestDto request) {
		ResponseEntity<String> response;

		try {
			Video video = videoService.createVideo(request);
			response = ResponseEntity.ok("영상이 성공적으로 추가되었습니다.");
		} catch (EntityExistsException e) {
			// 이미 존재하는 영상일 때 발생하는 예외 처리
			response = ResponseEntity.status(HttpStatus.CONFLICT)
				.body("이미 존재하는 영상입니다.");
		} catch (AccessDeniedException e) {
			// 사용자가 권한이 없을 때 발생하는 예외 처리
			response = ResponseEntity.status(HttpStatus.FORBIDDEN)
				.body("영상 추가 권한이 없습니다.");
		} catch (Exception e) {
			// 그 외 예외 처리
			response = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
				.body("서버 내부 오류가 발생했습니다.");
		}

		return response;
	}

	@Operation(summary = "영상 삭제 -> 어드민만 가능")
	@DeleteMapping("")
	public ResponseEntity<String> deleteVideo(@RequestParam Long videoId) {
		ResponseEntity<String> response;

		try {
			videoService.deleteVideo(videoId);
			response = ResponseEntity.ok("동영상이 삭제되었습니다.");
		} catch (NotFoundException e) {
			// 동영상이 존재하지 않을 때 발생하는 예외 처리
			response = ResponseEntity.status(HttpStatus.NOT_FOUND)
				.body("삭제하려는 동영상을 찾을 수 없습니다.");
		} catch (AccessDeniedException e) {
			// 사용자가 권한이 없을 때 발생하는 예외 처리
			response = ResponseEntity.status(HttpStatus.FORBIDDEN)
				.body("동영상을 삭제할 권한이 없습니다.");
		} catch (Exception e) {
			// 그 외 예외 처리
			response = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
				.body("서버 내부 오류가 발생했습니다.");
		}

		return response;
	}
}
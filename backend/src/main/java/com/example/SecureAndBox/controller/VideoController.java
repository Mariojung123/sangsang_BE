package com.example.SecureAndBox.controller;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.SecureAndBox.dto.VideoRequestDto;
import com.example.SecureAndBox.entity.Video;
import com.example.SecureAndBox.service.VideoService;

import io.swagger.v3.oas.annotations.Operation;
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
	public ResponseEntity<?> createVideo(@RequestBody VideoRequestDto request) {
		try {
			Video video = videoService.createVideo(request);
			return ResponseEntity.ok("영상이 성공적으로 추가되었습니다.");
		} catch (Exception e) {//sparrow - return 문이 catch 문 내에서 사용됩니다.  , 부적절한 예외처리
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("올바르지 않은 접근입니다.");
		}
	}

	@Operation(summary = "영상 삭제 -> 어드민만 가능")
	@DeleteMapping("")
	public ResponseEntity<?> deleteVideo(@RequestParam Long videoId) {
		try {
			videoService.deleteVideo(videoId);
			return ResponseEntity.ok("동영상이 삭제되었습니다.");
		} catch (Exception e) { //sparrow - return 문이 catch 문 내에서 사용됩니다.  , 부적절한 예외처리
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("올바르지 않은 접근입니다.");
		}
	}
}
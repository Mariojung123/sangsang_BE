package com.example.SecureAndBox.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PostResponseDto {
	private String title;

	private String username;

	private Long parent;

	@JsonProperty("created_at")
	private LocalDateTime updatedAt;

	private boolean isMe;
}

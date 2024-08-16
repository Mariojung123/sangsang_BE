package com.example.SecureAndBox.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PostDetailsResponseDto {
	private Long postId;

	private String title;
	@JsonProperty("description")
	private String content;
	private String username;
	private Long parent;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime updatedAt;

	private boolean isMe;
}

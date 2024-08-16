package com.example.SecureAndBox.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CommentRequestDto {

	private String content;
	private Long postId;



}

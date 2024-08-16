package com.example.SecureAndBox.dto;

import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CommentRequestDto {

	@Pattern(regexp = "^[ㄱ-ㅎ가-힣a-zA-Z0-9!?,.]*$")
	private String content;
	private Long postId;



}

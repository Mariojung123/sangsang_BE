package com.example.SecureAndBox.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PostRequestDto {
	@Pattern(regexp = "^[ㄱ-ㅎ가-힣a-zA-Z0-9!?,.]*$")
	private String title;
	@Pattern(regexp = "^[ㄱ-ㅎ가-힣a-zA-Z0-9!?,.]*$")
	@JsonProperty("description")
	private String content;

	private Long parent;

}

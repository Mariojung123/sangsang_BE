package com.example.SecureAndBox.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PostRequestDto {

	private String title;
	@JsonProperty("description")
	private String content;

	private Long parent;

}

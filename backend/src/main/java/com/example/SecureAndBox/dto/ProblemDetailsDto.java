package com.example.SecureAndBox.dto;

import com.example.SecureAndBox.etc.LanguageType;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Data
@Getter
@Builder
public class ProblemDetailsDto {
	private Long problemId;

	private String topic;

	private String title;

	private String difficulty;

	private String description;

	private LanguageType type;

	private String skeletonCode;
}

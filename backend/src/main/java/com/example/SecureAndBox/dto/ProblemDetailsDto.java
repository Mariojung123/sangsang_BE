package com.example.SecureAndBox.dto;

import java.util.List;

import com.example.SecureAndBox.etc.LanguageType;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Data
@Getter
@Builder
public class ProblemDetailsDto {
	@JsonProperty("pid")
	private Long problemId;

	private String topic;

	private List<Tag> tag;

	private String title;

	private String description;

	private Type type;


	public class Tag{
		private String variant;
		private String value;
	}

	public class Type{
		private String php;
		private String python;
	}
}

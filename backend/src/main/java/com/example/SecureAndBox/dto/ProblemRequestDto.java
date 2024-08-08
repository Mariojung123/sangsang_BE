package com.example.SecureAndBox.dto;

import java.util.List;
import java.util.Map;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;



@Data
@Getter
@Builder
public class ProblemRequestDto {

	private String image;
	private List<Tag> tag;
	private String title;
	private String description;
	private String topic;
	private Type type;
	private String difficulty;

	@Data
	@Getter
	@Builder
	public static class Tag {
		private String variant;
		private String value;
	}

	@Data
	@Getter
	@Builder
	public static class Type {
		private String php;
		private String python;
	}
}

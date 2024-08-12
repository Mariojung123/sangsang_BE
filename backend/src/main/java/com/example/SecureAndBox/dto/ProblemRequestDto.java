package com.example.SecureAndBox.dto;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import lombok.Builder;
import lombok.Getter;



@Getter
public class ProblemRequestDto {

	private String image;
	private List<Tag> tag;
	private String title;
	private String description;
	private String topic;
	private Type type;
	private String difficulty;


	@Getter
	@Builder
	public static class Tag {
		private String variant;
		private String value;
	}


	@Getter
	@Builder
	public static class Type {
		private String php;
		private String python;
	}

	public List<Tag> getTag() {
		return tag != null ? new ArrayList<>(tag) : Collections.emptyList();
	}

}

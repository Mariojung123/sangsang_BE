package com.example.SecureAndBox.dto;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;
import lombok.Getter;
@Builder
@Getter
public class ProblemResponseDto {
	@JsonProperty("pid")
	private Long problemId;
	private String image;
	private List<Tag> tags;
	private String title;
	private String description;
	private String topic;

	@Getter
	@Builder
	public static class Tag {
		private String variant;
		private String value;
	}

	public List<Tag> getTag() {
		return tags != null ? new ArrayList<>(tags) : Collections.emptyList();
	}



}

package com.example.SecureAndBox.dto;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;


@Getter
@Builder
public class ProblemDetailsDto {

	@JsonProperty("pid")
	private Long problemId;

	private String topic;

	private List<Tag> tags;

	private String title;

	private String description;

	private Type type;

	private String image;

	@Data
	@Builder
	@AllArgsConstructor
	public static class Tag {
		private String variant;
		private String value;
	}

	@Data
	@Builder
	@AllArgsConstructor
	public static class Type {
		private String php;
		private String python;

	}

	public List<Tag> getTags() {
		return tags != null ? new ArrayList<>(tags) : Collections.emptyList();
	}

	public Type getType() {
		return type != null ? new Type(type.getPhp(), type.getPython()) : null;
	}

	public static class ProblemDetailsDtoBuilder {
		private List<Tag> tags;
		private Type type;

		public ProblemDetailsDtoBuilder tags(List<Tag> tags) {
			this.tags = tags != null ? new ArrayList<>(tags) : null;
			return this;
		}

		public ProblemDetailsDtoBuilder type(Type type) {
			this.type = type != null ? new Type(type.getPhp(), type.getPython()) : null;
			return this;
		}
	}


}

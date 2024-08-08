package com.example.SecureAndBox.entity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.Type;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "problem")
@Getter
@Builder
@NoArgsConstructor
public class Problem {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long problemId;

	private String topic;

	private String title;

	private String difficulty;

	private String description;

	private String image;

	@Column(name = "tags", columnDefinition = "json")
	@JdbcTypeCode(SqlTypes.JSON)
	private List<Map<String, String>> tags;

	@Column(name = "type", columnDefinition = "json")
	@JdbcTypeCode(SqlTypes.JSON)
	private Map<String, String> type;


	public Map<String, String> getType() {
		return type != null ? Collections.unmodifiableMap(type) : Collections.emptyMap();
	}

	public List<Map<String, String>> getTags() {
		return tags != null ? Collections.unmodifiableList(tags) : Collections.emptyList();
	}



	@Builder
	public Problem(Long problemId, String topic, String title, String difficulty, String description,
		String image, List<Map<String, String>> tags, Map<String, String> type) {
		this.problemId = problemId;
		this.topic = topic;
		this.title = title;
		this.difficulty = difficulty;
		this.description = description;
		this.image = image;
		this.tags = tags != null ? Collections.unmodifiableList(new ArrayList<>(tags)) : null;
		this.type = type != null ? Collections.unmodifiableMap(new HashMap<>(type)) : null;
	}

	public static class ProblemBuilder {
		private List<Map<String, String>> tags;
		private Map<String, String> type;

		public ProblemBuilder tags(List<Map<String, String>> tags) {
			this.tags = tags != null ? new ArrayList<>(tags) : null;
			return this;
		}

		public ProblemBuilder type(Map<String, String> type) {
			this.type = type != null ? new HashMap<>(type) : null;
			return this;
		}
	}
}
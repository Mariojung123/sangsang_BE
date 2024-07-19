package com.example.SecureAndBox.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Getter
public class CodeSubmission {

	@JsonProperty("problem_id")
	private Long problemId;
	@JsonProperty("language_type")
	private String languageType;
	@JsonProperty("user_code")
	private String userCode;

}

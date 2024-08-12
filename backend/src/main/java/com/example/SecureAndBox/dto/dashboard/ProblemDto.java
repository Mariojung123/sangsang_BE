package com.example.SecureAndBox.dto.dashboard;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@Builder
public class ProblemDto {

	private Long problemId;

	private String title;

	private String topic;
}

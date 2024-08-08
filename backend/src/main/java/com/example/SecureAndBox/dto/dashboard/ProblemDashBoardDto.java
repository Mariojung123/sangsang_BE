package com.example.SecureAndBox.dto.dashboard;

import java.util.Collections;
import java.util.List;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Data
@Getter
@Builder
public class ProblemDashBoardDto {
	private List<ProblemDto> problemList;
	private int count;

	public List<ProblemDto> getProblemList() {
		return problemList != null ? Collections.unmodifiableList(problemList) : Collections.emptyList();
	}

}

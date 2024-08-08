package com.example.SecureAndBox.dto.dashboard;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;


@Getter
@Builder
public class ProblemDashBoardDto {
	private List<ProblemDto> problemList;
	private int count;

	public List<ProblemDto> getProblemList() {
		return problemList != null ? Collections.unmodifiableList(problemList) : Collections.emptyList();
	}

	public static class ProblemDashBoardDtoBuilder {
		private List<ProblemDto> problemList;

		public ProblemDashBoardDtoBuilder problemList(List<ProblemDto> problemList) {
			this.problemList = problemList != null ? new ArrayList<>(problemList) : null;
			return this;
		}
	}

}

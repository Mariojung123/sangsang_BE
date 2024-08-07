package com.example.SecureAndBox.dto.dashboard;

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

}

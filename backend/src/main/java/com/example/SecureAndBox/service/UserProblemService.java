package com.example.SecureAndBox.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.SecureAndBox.dto.dashboard.ProblemDashBoardDto;
import com.example.SecureAndBox.dto.dashboard.ProblemDto;
import com.example.SecureAndBox.entity.Problem;
import com.example.SecureAndBox.entity.User;
import com.example.SecureAndBox.entity.UserProblemRelation;
import com.example.SecureAndBox.repository.UserProblemRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class UserProblemService {

	private final UserProblemRepository userProblemRepository;

	public ProblemDashBoardDto getProblem(User user) {
		// Retrieve all UserProblemRelation entities for the given user
		List<UserProblemRelation> userProblemRelations = userProblemRepository.findAllByUser(user);

		// Map each UserProblemRelation to a ProblemDto
		List<ProblemDto> problemList = userProblemRelations.stream()
			.map(relation -> {
				Problem problem = relation.getProblem();
				return ProblemDto.builder()
					.problemId(problem.getProblemId())
					.title(problem.getTitle())
					.topic(problem.getTopic())
					.build();
			})
			.collect(Collectors.toList());

		ProblemDashBoardDto dto = ProblemDashBoardDto.builder()
			.problemList(problemList)
			.count(problemList.size())
			.build();
		return dto;

	}
}

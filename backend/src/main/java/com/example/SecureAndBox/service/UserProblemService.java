package com.example.SecureAndBox.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.SecureAndBox.dto.dashboard.ProblemDashBoardDto;
import com.example.SecureAndBox.dto.dashboard.ProblemDto;
import com.example.SecureAndBox.entity.Problem;
import com.example.SecureAndBox.entity.User;
import com.example.SecureAndBox.entity.UserProblemRelation;
import com.example.SecureAndBox.exception.post.NotFoundProblemException;
import com.example.SecureAndBox.exception.user.NotFoundUserException;
import com.example.SecureAndBox.login.exception.CustomException;
import com.example.SecureAndBox.repository.UserProblemRepository;

import jakarta.transaction.Transactional;
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

		// 방어적 복사를 통해 전달된 리스트의 새로운 복사본을 생성
		List<ProblemDto> copiedProblemList = problemList != null ? new ArrayList<>(problemList) : new ArrayList<>();

		// ProblemDashBoardDto 객체를 생성하면서 복사본을 사용
		ProblemDashBoardDto dto = ProblemDashBoardDto.builder()
			.problemList(copiedProblemList)
			.count(copiedProblemList.size())
			.build();

		return dto;
	}
	@Transactional
	public void saveRelation(UserProblemRelation up) {

		if(up.getUser()==null)
		{
			throw new NotFoundUserException();
		}

		if(up.getProblem()==null)
		{
			throw new NotFoundProblemException();
		}
		userProblemRepository.save(up);
	}

	@Transactional
	public UserProblemRelation createUserProblem(User user, Problem problem)
	{	if(user==null)
		{
			throw new NotFoundUserException();
		}

		if(problem==null)
		{
			throw new NotFoundProblemException();
		}

		UserProblemRelation up = UserProblemRelation.builder()
			.user(user)
			.problem(problem)
			.build();
		return up;

	}

}

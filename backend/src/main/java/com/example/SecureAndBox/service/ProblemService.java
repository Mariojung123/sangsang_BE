package com.example.SecureAndBox.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.SecureAndBox.entity.Problem;
import com.example.SecureAndBox.repository.ProblemRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProblemService {

	private final ProblemRepository problemRepository;
	public String getProblemType(Long promId,String languageType)
	{
		//problemId를 통해 DB에서 문제 조회 하여 반환
		return "login";
	}



	String getImageNameForProblemType(Long promId, String languageType) {

		// Problem problem = getProblemType(promId,languageType);

		String problemType = getProblemType(promId,languageType);
		switch (problemType) {
			case "login":
				return "login-secure-code-image";
			case "post":
				return "post-secure-code-image";
			default:
				throw new IllegalArgumentException("Unknown problem type: " + problemType);
		}
	}

	public Object getProblemList(Pageable pageable) {
  		List<Problem> problems = problemRepository.findAll(pageable).getContent();
		return problems;

	}

	public Object getProblemListByDifficulty(String difficulty) {
		List<Problem> problems = problemRepository.findAllByDifficulty(difficulty);
		return problems;

	}

	public Object getProblemListByTopic(String topic) {
		List<Problem> problems = problemRepository.findAllByTopic(topic);
		return problems;
	}
}

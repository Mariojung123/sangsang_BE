package com.example.SecureAndBox.service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;

import com.example.SecureAndBox.dto.ProblemDetailsDto;
import com.example.SecureAndBox.entity.Problem;
import com.example.SecureAndBox.etc.LanguageType;
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

	public Problem findById(Long id)
	{
		return problemRepository.findByProblemId(id);
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

	public String getSkeletonCode(String filename, LanguageType type) throws IOException {
		Resource resource = new ClassPathResource("static/" + filename+"/"+ type.getKey()+"/"+filename+"."+type.getKey());
		byte[] bdata = FileCopyUtils.copyToByteArray(resource.getInputStream());
		return new String(bdata, StandardCharsets.UTF_8);
	}

	public Object getProblem(Long problemId, LanguageType type) throws IOException {
		Problem problem = problemRepository.findByProblemId(problemId);
		ProblemDetailsDto dto = ProblemDetailsDto.builder()
			.problemId(problemId)
			.title(problem.getTitle())
			//.difficulty(problem.getDifficulty())
			.topic(problem.getTopic())
			.description(problem.getDescription())
		//	.skeletonCode(getSkeletonCode(problem.getTitle(),type))
			.build();
		return dto;
	}
}

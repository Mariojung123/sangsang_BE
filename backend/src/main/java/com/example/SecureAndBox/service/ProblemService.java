package com.example.SecureAndBox.service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.AccessDeniedException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;

import com.example.SecureAndBox.dto.ProblemDetailsDto;
import com.example.SecureAndBox.dto.ProblemRequestDto;
import com.example.SecureAndBox.entity.Problem;
import com.example.SecureAndBox.entity.User;
import com.example.SecureAndBox.etc.LanguageType;
import com.example.SecureAndBox.exception.ProblemNotFoundException;
import com.example.SecureAndBox.repository.ProblemRepository;

import jakarta.transaction.Transactional;
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
		return problemRepository.findByProblemId(id).get();
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

	public Object getProblem(Long problemId, LanguageType languageType) throws IOException {
		Problem problem = getProblemById(problemId);

		List<ProblemDetailsDto.Tag> tags = problem.getTags().stream()
			.map(tagMap -> ProblemDetailsDto.Tag.builder()
				.variant(tagMap.get("variant"))
				.value(tagMap.get("value"))
				.build())
			.collect(Collectors.toList());

		ProblemDetailsDto.Type type = ProblemDetailsDto.Type.builder()
			.php(problem.getType().get("php"))
			.python(problem.getType().get("python"))
			.build();

		ProblemDetailsDto dto = ProblemDetailsDto.builder()
			.problemId(problemId)
			.title(problem.getTitle())
			.topic(problem.getTopic())
			.image(problem.getImage())
			.description(problem.getDescription())
			.tags(tags)
			.type(type)
			.build();

		return dto;
	}

	@Transactional
	public Problem createProblem(User user, ProblemRequestDto problemRequestDto) throws AccessDeniedException {
		if(user.getRole()!= User.Role.ADMIN)
		{
			throw new AccessDeniedException("관리자 권한이 부족합니다.");
		}

		List<Map<String, String>> tags = problemRequestDto.getTag().stream()
			.map(tag -> Map.of("variant", tag.getVariant(), "value", tag.getValue()))
			.collect(Collectors.toList());

		Map<String, String> type = Map.of(
			"php", problemRequestDto.getType().getPhp(),
			"python", problemRequestDto.getType().getPython()
		);

		Problem problem = Problem.builder()
			.topic(problemRequestDto.getTopic())
			.title(problemRequestDto.getTitle())
			.difficulty(problemRequestDto.getDifficulty())  // default value
			.description(problemRequestDto.getDescription())
			.image(problemRequestDto.getImage())
			.tags(tags)
			.type(type)
			.build();

		return problemRepository.save(problem);
	}
	public Problem getProblemById(Long problemId) {
		Optional<Problem> optionalProblem = problemRepository.findByProblemId(problemId);
		return optionalProblem.orElseThrow(() -> new ProblemNotFoundException("Problem not found"));
	}
}

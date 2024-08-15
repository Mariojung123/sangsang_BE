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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;

import com.example.SecureAndBox.dto.ProblemDetailsDto;
import com.example.SecureAndBox.dto.ProblemRequestDto;
import com.example.SecureAndBox.dto.ProblemResponseDto;
import com.example.SecureAndBox.entity.Problem;
import com.example.SecureAndBox.entity.User;
import com.example.SecureAndBox.etc.LanguageType;
import com.example.SecureAndBox.exception.ProblemNotFoundException;
import com.example.SecureAndBox.login.exception.CustomException;
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
		Page<Problem> problemPage = problemRepository.findAll(pageable);
		List<Problem> problems = problemPage.getContent();

		// Map each Problem to a ProblemResponseDto
		List<ProblemResponseDto> problemResponseList = problems.stream()
			.map(problem -> {
				List<ProblemResponseDto.Tag> tags = problem.getTags().stream()
					.map(tagMap -> ProblemResponseDto.Tag.builder()
						.variant(tagMap.get("variant"))
						.value(tagMap.get("value"))
						.build())
					.collect(Collectors.toList());

				return ProblemResponseDto.builder()
					.problemId(problem.getProblemId())
					.title(problem.getTitle())
					.topic(problem.getTopic())
					.description(problem.getDescription())
					.image(problem.getImage())
					.tags(tags)
					.build();
			})
			.collect(Collectors.toList());

		return problemResponseList;
	}

	/*public Object getProblemListByDifficulty(String difficulty) {
		List<Problem> problems = problemRepository.findAllByDifficulty(difficulty);
		return problems;

	}

	public Object getProblemListByTopic(String topic) {
		List<Problem> problems = problemRepository.findAllByTopic(topic);
		return problems;
	}*/

	/*public String getSkeletonCode(String topic, String title, LanguageType type) throws IOException {
		// 리소스 경로를 String.format으로 좀 더 읽기 쉽게 구성
		String resourcePath = String.format("static/problem/%s/%s/%s.%s",
			topic, type.getKey(), title, type.getKey());
		Resource resource = new ClassPathResource(resourcePath);

		// 파일 데이터를 읽고 String으로 변환
		byte[] bdata = FileCopyUtils.copyToByteArray(resource.getInputStream());
		return new String(bdata, StandardCharsets.UTF_8);
	}*/

	public String getSkeletonCode(Map<String, String> code, String type) throws IOException {
		// 해당 타입의 코드를 가져옴
		String skeletonCode = code.get(type);

		// 만약 가져온 코드가 null이면 예외를 던짐
		if (skeletonCode == null) {
			throw new IOException("Skeleton code not found for type: " + type);
		}

		return skeletonCode;
	}

	public Object getProblem(Long problemId) throws IOException {
		// 문제를 ID로 조회
		Problem problem = getProblemById(problemId);

		// 태그 리스트를 DTO로 변환
		List<ProblemDetailsDto.Tag> tags = problem.getTags().stream()
			.map(tagMap -> ProblemDetailsDto.Tag.builder()
				.variant(tagMap.get("variant"))
				.value(tagMap.get("value"))
				.build())
			.collect(Collectors.toList());

		// 스켈레톤 코드 가져오기
		String phpCode = getSkeletonCode(problem.getType(),"php");
		String pythonCode = getSkeletonCode(problem.getType(), "python");

		// 타입 정보 생성
		ProblemDetailsDto.Type type = ProblemDetailsDto.Type.builder()
			.php(phpCode)
			.python(pythonCode)
			.build();

		// 문제 DTO 생성 및 반환
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

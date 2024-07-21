package com.example.SecureAndBox.service;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Path;


import com.example.SecureAndBox.dto.CodeSubmission;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SecureCodeService {
	private final ProblemService problemService;
	public String testSecureCode(Path codePath, CodeSubmission dto) throws IOException, InterruptedException {

		//User user = userService.findByUserId(userid);
		//문제 ID와 languageType으로 문제DB에 저장된 문제를 인지하고 이미지 이름을 지정하여 빌드하도록 한다.
		String imageName = problemService.getImageNameForProblemType(dto.getProblemId(),dto.getLanguageType());
		//이미지 ID는 미리 문제 도커 이미지를 Docker나 서버 파일 시스템에 업로드 해놓고 문제마다 바로 사용할 수 있도록 한다.


		// Docker 컨테이너 실행하여 코드 빌드 및 테스트
		//리소스 제한, 네트워크 격리, 읽기전용 파일시스템,커널기능제한
		ProcessBuilder processBuilder = new ProcessBuilder(
			"docker", "run", "--rm", "--network", "none", "--read-only",
			"-m", "256m", "--cpus", "0.5", "--cap-drop", "all",
			"-v", codePath.toAbsolutePath() + ":/app/src/main/java/com/example/SecureCode.java",
			imageName, "/bin/sh", "-c",
			"javac /app/src/main/java/com/example/SecureCode.java && java -cp /app/src/main/java com.example.SecureCode"
		);
		//네트워크를 격리시켰기 때문에 결과를 response 받기 위해선
		//컨테이너 내부의 실행 결과를 호스트로 리다이렉트하여 실행 결과를 수집합니다.
		Process process = processBuilder.start();

		// 실행 결과를 읽기
		BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
		StringBuilder result = new StringBuilder();
		String line;
		while ((line = reader.readLine()) != null) {
			result.append(line).append("\n");
		}

		int exitCode = process.waitFor();

		if (exitCode == 0) {
			return "Secure code implementation is valid.\n" + result.toString();
		} else {
			return "Secure code implementation failed.\n" + result.toString();
		}
	}

}
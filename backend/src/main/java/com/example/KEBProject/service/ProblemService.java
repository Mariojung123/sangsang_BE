package com.example.KEBProject.service;

import org.springframework.stereotype.Service;


import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProblemService {

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
}

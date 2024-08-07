package com.example.SecureAndBox.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.SecureAndBox.entity.User;
import com.example.SecureAndBox.login.interceptor.UserId;
import com.example.SecureAndBox.service.UserProblemService;

import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class UserProblemController {
	private final UserProblemService userProblemService;
	@GetMapping("/problem")
	public ResponseEntity<?> getProblem(
		@Parameter(hidden = true) @UserId User user
	)
	{
		return ResponseEntity.ok(userProblemService.getProblem(user));
	}


}

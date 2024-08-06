package com.example.SecureAndBox.login.application;

import org.springframework.stereotype.Service;

import com.example.SecureAndBox.entity.User;
import com.example.SecureAndBox.exception.NotFoundUserException;
import com.example.SecureAndBox.oauth.utils.JwtUtil;
import com.example.SecureAndBox.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

	private final UserRepository userRepository;
	private final JwtUtil jwtUtil;

	public User findByUserId(Long userId) {
		return userRepository.findByUserId(userId)
			.orElseThrow(NotFoundUserException::new);
	}


}
package com.example.SecureAndBox.login.application;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.SecureAndBox.entity.User;
import com.example.SecureAndBox.exception.NotFoundUserException;
import com.example.SecureAndBox.login.dto.request.SignUpDto;
import com.example.SecureAndBox.oauth.utils.JwtUtil;
import com.example.SecureAndBox.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

	private final UserRepository userRepository;


	public User findByUserId(Long userId) {
		return userRepository.findByUserId(userId)
			.orElseThrow(NotFoundUserException::new);
	}




}
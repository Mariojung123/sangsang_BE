package com.example.SecureAndBox.controller;

import com.example.SecureAndBox.entity.User;
import com.example.SecureAndBox.repository.UserRepository;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserRepository userRepository;



}



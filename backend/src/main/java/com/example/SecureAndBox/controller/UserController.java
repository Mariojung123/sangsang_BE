package com.example.SecureAndBox.controller;

import com.example.SecureAndBox.entity.User;
import com.example.SecureAndBox.repository.UserRepository;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

public class UserController {
    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PostMapping("/users/save")
    public void personSave(@RequestBody User user) {
        userRepository.save(user);

    }
}



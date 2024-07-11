package com.example.KEBProject.controller;

import com.example.KEBProject.entity.User;
import com.example.KEBProject.repository.UserRepository;
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



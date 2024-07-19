package com.example.KEBProject.controller;

import com.example.KEBProject.entity.User;
import com.example.KEBProject.repository.UserRepository;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserRepository userRepository;

   // private final UserService userService;

    @PostMapping("/users/save")
    public void personSave(@RequestBody User user) {
        userRepository.save(user);

    }


}



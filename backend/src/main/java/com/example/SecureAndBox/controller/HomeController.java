package com.example.SecureAndBox.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class HomeController {
    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("message", "Welcome to KguSandBox!");
        return "index"; // index.html 템플릿을 반환
    }
    @GetMapping("/test/call/{name}")
    @ResponseBody
    public String testApi(@PathVariable String name) {
        return "Hello " + name;
    }
}

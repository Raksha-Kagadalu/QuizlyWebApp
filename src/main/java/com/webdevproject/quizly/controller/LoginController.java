package com.webdevproject.quizly.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    // To display custom login page for the quizly app
    @GetMapping("/userLogin")
    public String userLogin(){
        return "login";
    }

    // To create custom access denied page
    @GetMapping("/access-denied")
    public String accessDenied(){
        return "access-denied";
    }
}
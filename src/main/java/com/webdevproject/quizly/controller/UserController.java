package com.webdevproject.quizly.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Set;

    @Controller
    public class UserController {

        // To redirect user to respective dashboards based on user role
        @GetMapping("/")
        public String showDashboard() {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            Set<String> roles = AuthorityUtils.authorityListToSet(auth.getAuthorities());

            if (roles.contains("ADMIN")) {
                return "redirect:/admin/dashboard";
            } else if (roles.contains("PROFESSOR")) {
                return "redirect:/professor/dashboard";
            } else if (roles.contains("STUDENT")) {
                return "redirect:/student/dashboard";
            }
            return "access-denied";
        }

        }

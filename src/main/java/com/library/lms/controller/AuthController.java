package com.library.lms.controller;

import com.library.lms.model.User;
import com.library.lms.service.UserService;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    // POST /auth/login → Login user
    @PostMapping("/login")
    public User login(@RequestBody Map<String, String> loginRequest) {
        String email = loginRequest.get("email");
        String password = loginRequest.get("password");
        return userService.login(email, password);
    }
}

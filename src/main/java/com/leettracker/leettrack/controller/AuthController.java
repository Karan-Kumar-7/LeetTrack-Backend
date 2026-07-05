package com.leettracker.leettrack.controller;

import com.leettracker.leettrack.dto.LoginRequest;
import com.leettracker.leettrack.dto.LoginResponse;
import com.leettracker.leettrack.dto.RegisterRequest;
import com.leettracker.leettrack.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @PostMapping("/register")
    public String register(@RequestBody RegisterRequest request) {

        userService.register(request);

        return "User registered successfully!";
    }

    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest request) {
        return userService.login(request);
    }
}
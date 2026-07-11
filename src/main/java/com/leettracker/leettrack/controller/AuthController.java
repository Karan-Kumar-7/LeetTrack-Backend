package com.leettracker.leettrack.controller;

import com.leettracker.leettrack.dto.LoginRequest;
import com.leettracker.leettrack.dto.LoginResponse;
import com.leettracker.leettrack.dto.RegisterRequest;
import com.leettracker.leettrack.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "APIs for user registration and login")
public class AuthController {

    private final UserService userService;

    @Operation(
            summary = "Register a new user",
            description = "Creates a new user account with an encrypted password."
    )
    @PostMapping("/register")
    public String register(@Valid @RequestBody RegisterRequest request) {

        userService.register(request);

        return "User registered successfully!";
    }

    @Operation(
            summary = "Login and receive JWT",
            description = "Authenticates the user and returns a JWT token."
    )
    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest request) {
        return userService.login(request);
    }
}
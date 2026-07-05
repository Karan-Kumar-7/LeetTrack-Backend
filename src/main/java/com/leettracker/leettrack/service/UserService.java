package com.leettracker.leettrack.service;

import com.leettracker.leettrack.dto.LoginRequest;
import com.leettracker.leettrack.dto.LoginResponse;
import com.leettracker.leettrack.dto.RegisterRequest;

public interface UserService {

    void register(RegisterRequest request);
    LoginResponse login(LoginRequest request);
}

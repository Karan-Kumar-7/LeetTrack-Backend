package com.leettracker.leettrack.service.impl;

import com.leettracker.leettrack.dto.RegisterRequest;
import com.leettracker.leettrack.entity.User;
import com.leettracker.leettrack.repository.UserRepository;
import com.leettracker.leettrack.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    @Override
    public void register(RegisterRequest request)
    {
        System.out.println("Register method called");

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        userRepository.save(user);

        System.out.println("User saved successfully");
    }
}

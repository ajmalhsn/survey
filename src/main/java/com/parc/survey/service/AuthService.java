package com.parc.survey.service;

import org.springframework.stereotype.Service;

import com.parc.survey.domain.User;


import com.parc.survey.dto.LoginRequest;

import com.parc.survey.dto.RegisterRequest;
import com.parc.survey.dto.UserResponse;

import com.parc.survey.repository.UserRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    
    public UserResponse login(LoginRequest request) {
        User user = userRepository.findByUsername(request.getUsername())
            .orElseThrow(() -> new RuntimeException("User not found"));
        
        if (!user.getPassword().equals(request.getPassword())) {
            throw new RuntimeException("Invalid password");
        }
        
        return mapToUserResponse(user);
    }
    
    @Transactional
    public void register(RegisterRequest request, boolean isAdmin) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("Username already exists");
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already exists");
        }
        
        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());
        user.setRole(isAdmin ? User.Role.ADMIN : User.Role.USER);
        
        userRepository.save(user);
    }
    
    private UserResponse mapToUserResponse(User user) {
        return new UserResponse(
            user.getId(),
            user.getUsername(),
            user.getEmail(),
            user.getRole().name()
        );
    }
}

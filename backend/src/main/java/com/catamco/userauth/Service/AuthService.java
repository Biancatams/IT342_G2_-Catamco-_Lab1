package com.miniapp.service;

import com.miniapp.config.TokenProvider;
import com.miniapp.entity.UserEntity;
import com.miniapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private TokenProvider tokenProvider;

    // registerUser(user: UserEntity): UserEntity
    public Map<String, Object> registerUser(UserEntity user) {
        // Check if username exists
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new RuntimeException("Username already exists");
        }

        // Check if email exists
        if (userRepository.existsByEmailAddress(user.getEmailAddress())) {
            throw new RuntimeException("Email already exists");
        }

        // Encode password
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // Save user
        UserEntity savedUser = userRepository.save(user);

        // Generate token
        String token = tokenProvider.generateToken(savedUser.getUsername());

        // Return response
        Map<String, Object> response = new HashMap<>();
        response.put("token", token);
        response.put("userId", savedUser.getUserId());
        response.put("username", savedUser.getUsername());
        response.put("emailAddress", savedUser.getEmailAddress());
        response.put("firstName", savedUser.getFirstName());
        response.put("lastName", savedUser.getLastName());

        return response;
    }

    // authenticateUser(email: String, password: String): UserEntity
    public Map<String, Object> authenticateUser(String username, String password) {
        Optional<UserEntity> userOpt = userRepository.findByUsername(username);

        if (userOpt.isEmpty()) {
            throw new RuntimeException("Invalid username or password");
        }

        UserEntity user = userOpt.get();

        // Verify password
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("Invalid username or password");
        }

        // Generate token
        String token = tokenProvider.generateToken(user.getUsername());

        // Return response
        Map<String, Object> response = new HashMap<>();
        response.put("token", token);
        response.put("userId", user.getUserId());
        response.put("username", user.getUsername());
        response.put("emailAddress", user.getEmailAddress());
        response.put("firstName", user.getFirstName());
        response.put("lastName", user.getLastName());

        return response;
    }

    // validateToken(token: String): boolean
    public boolean validateToken(String token) {
        return tokenProvider.validateToken(token);
    }

    // getUserByUsername(username: String): UserEntity
    public UserEntity getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
}

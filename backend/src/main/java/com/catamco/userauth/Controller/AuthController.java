package com.miniapp.controller;

import com.miniapp.config.TokenProvider;
import com.miniapp.entity.UserEntity;
import com.miniapp.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private TokenProvider tokenProvider;

    // register(UserEntity user)
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody UserEntity user) {
        try {
            Map<String, Object> response = authService.registerUser(user);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    // login(String email, String password)
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> loginRequest) {
        try {
            String username = loginRequest.get("username");
            String password = loginRequest.get("password");

            Map<String, Object> response = authService.authenticateUser(username, password);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
        }
    }

    // getCurrentUser()
    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(@RequestHeader("Authorization") String authHeader) {
        try {
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                throw new RuntimeException("Invalid authorization header");
            }

            String token = authHeader.substring(7);

            if (!authService.validateToken(token)) {
                throw new RuntimeException("Invalid or expired token");
            }

            String username = tokenProvider.getUsernameFromToken(token);
            UserEntity user = authService.getUserByUsername(username);

            Map<String, Object> response = new HashMap<>();
            response.put("userId", user.getUserId());
            response.put("firstName", user.getFirstName());
            response.put("lastName", user.getLastName());
            response.put("username", user.getUsername());
            response.put("emailAddress", user.getEmailAddress());
            response.put("createdAt", user.getCreatedAt());

            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
        }
    }
}
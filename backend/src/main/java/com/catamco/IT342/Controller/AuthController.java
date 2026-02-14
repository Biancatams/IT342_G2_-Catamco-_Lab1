package com.catamco.IT342.Controller;

import com.catamco.IT342.Entity.User;
import com.catamco.IT342.Service.UserService;
import com.catamco.IT342.Security.TokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:3000")
public class AuthController {
    @Autowired private UserService userService;
    @Autowired private TokenProvider tokenProvider;

    @PostMapping("/register")
    public User register(@RequestBody User user) {
        return userService.registerUser(user);
    }

    @PostMapping("/login")
    public String login(@RequestBody Map<String, String> credentials) {
        return userService.authenticateUser(credentials.get("email"), credentials.get("password"));
    }

    @PostMapping("/logout")
    public void logout(@RequestParam String token) {
        userService.logoutUser(token);
    }

    @GetMapping("/me")
    public User getCurrentUser(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        int userId = tokenProvider.getUserIdFromToken(token);
        return userService.getCurrentUser(userId);
    }
}
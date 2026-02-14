package com.catamco.IT342.Service;

import com.catamco.IT342.Entity.User;
import com.catamco.IT342.Repository.AuthRepository;
import com.catamco.IT342.Security.TokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired private AuthRepository authRepository;
    @Autowired private TokenProvider tokenProvider;
    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public User registerUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return authRepository.save(user);
    }

    public String authenticateUser(String email, String password) {
        return authRepository.findByEmailAddress(email)
                .filter(u -> passwordEncoder.matches(password, u.getPassword()))
                .map(tokenProvider::generateToken)
                .orElse(null);
    }

    public void logoutUser(String token) {
        tokenProvider.invalidateToken(token);
    }

    public User getCurrentUser(int user_id) {
        return authRepository.findById(user_id).orElse(null);
    }

    public boolean checkUserExists(String email) {
        return authRepository.existsByEmailAddress(email);
    }

    public boolean validateCredentials(String email, String password) {
        return authRepository.findByEmailAddress(email)
                .map(u -> passwordEncoder.matches(password, u.getPassword()))
                .orElse(false);
    }
}
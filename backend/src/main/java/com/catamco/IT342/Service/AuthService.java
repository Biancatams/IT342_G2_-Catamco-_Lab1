package com.catamco.IT342.Service;

import com.catamco.IT342.DTO.RegisterDTO;
import com.catamco.IT342.DTO.LoginDTO;
import com.catamco.IT342.DTO.UserResponseDTO;
import com.catamco.IT342.Entity.User;
import com.catamco.IT342.Repository.UserRepository;
import com.catamco.IT342.Security.PasswordEncoder;
import com.catamco.IT342.Security.TokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private TokenProvider tokenProvider;

    public User registerUser(RegisterDTO registerDTO) {
        if (userRepository.existsByEmail(registerDTO.getEmailAddress())) {
            throw new RuntimeException("Email already registered");
        }

        User user = new User();
        user.setFirstName(registerDTO.getFirstName());
        user.setLastName(registerDTO.getLastName());
        user.setUsername(registerDTO.getUsername());
        user.setEmailAddress(registerDTO.getEmailAddress());

        user.setPassword(passwordEncoder.encode(registerDTO.getPassword()));

        return userRepository.save(user);
    }

    public String authenticateUser(LoginDTO loginDTO) {
        return userRepository.findByEmail(loginDTO.getEmail())
                .filter(user -> passwordEncoder.matches(loginDTO.getPassword(), user.getPassword()))
                .map(tokenProvider::generateToken)
                .orElse(null);
    }

    public void logoutUser(String token) {
        tokenProvider.invalidateToken(token);
    }

    public UserResponseDTO getCurrentUser(int userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        UserResponseDTO dto = new UserResponseDTO();
        dto.setUserId(user.getUserId());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setUsername(user.getUsername());
        dto.setEmailAddress(user.getEmailAddress());
        dto.setCreatedAt(user.getCreatedAt());

        return dto;
    }

    public boolean checkUserExists(String email) {
        return userRepository.existsByEmail(email);
    }

    public boolean validateCredentials(String email, String password) {
        return userRepository.findByEmail(email)
                .map(user -> passwordEncoder.matches(password, user.getPassword()))
                .orElse(false);
    }
}
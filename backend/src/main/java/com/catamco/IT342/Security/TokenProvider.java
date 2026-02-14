package com.catamco.IT342.Security;

import com.catamco.IT342.Entity.User;
import org.springframework.stereotype.Component;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Component
public class TokenProvider {
    private String jwtSecret = "it342_secret";
    private Long jwtExpiration = 86400000L;
    private Set<String> blacklistedTokens = new HashSet<>();
    private Map<String, Integer> tokenToUserIdMap = new HashMap<>();

    public String generateToken(User user) {
        String token = "SESSION_" + user.getEmail_address() + "_" + System.currentTimeMillis();
        tokenToUserIdMap.put(token, user.getUser_id());
        return token;
    }

    public boolean validateToken(String token) {
        return token != null && !blacklistedTokens.contains(token);
    }

    public int getUserIdFromToken(String token) {
        return tokenToUserIdMap.getOrDefault(token, 1);
    }

    public void invalidateToken(String token) {
        blacklistedTokens.add(token);
        tokenToUserIdMap.remove(token);
    }
}
package com.catamco.IT342.Security;

import com.catamco.IT342.Entity.User;
import org.springframework.stereotype.Component;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Component
public class TokenProvider {
    private final String jwtSecret = "it342_secret";
    private final Long jwtExpiration = 86400000L; // 24 hours

    private final Set<String> blacklistedTokens = new HashSet<>();

    private final Map<String, Integer> tokenToUserIdMap = new HashMap<>();

    public String generateToken(User user) {
        String token = "SESSION_" + user.getEmailAddress() + "_" + System.currentTimeMillis();
        tokenToUserIdMap.put(token, user.getUserId());
        return token;
    }

    public boolean validateToken(String token) {
        return token != null && !blacklistedTokens.contains(token) && tokenToUserIdMap.containsKey(token);
    }

    public int getUserIdFromToken(String token) {
        return tokenToUserIdMap.getOrDefault(token, -1);
    }

    public void invalidateToken(String token) {
        blacklistedTokens.add(token);
        tokenToUserIdMap.remove(token);
    }
}
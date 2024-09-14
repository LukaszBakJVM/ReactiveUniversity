package org.example.reactiveuniversity.security;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class TokenStore {
    private final Map<String, String> tokenMap = new HashMap<>();


    public void setToken(String email, String token) {
        tokenMap.put(email, token);

    }

    public String getToken(String email) {
        return tokenMap.get(email);
    }

}



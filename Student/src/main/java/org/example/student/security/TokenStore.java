package org.example.student.security;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class TokenStore {
    private final Map<String, String> tokenMap = new HashMap<>();
    @Value("${token.store.path}")
    private String tokenStore;


    @PostConstruct
    public void init() {
        read();


    }


    public String getToken(String email) {

        return tokenMap.get(email);


    }


    private void read() {
        try (var file = new FileReader(tokenStore); var buffer = new BufferedReader(file)) {

            tokenMap.putAll(buffer.lines().map(line -> line.split(" token-> ", 2)).collect(Collectors.toMap(k -> k[0], k -> k[1])));


        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

}


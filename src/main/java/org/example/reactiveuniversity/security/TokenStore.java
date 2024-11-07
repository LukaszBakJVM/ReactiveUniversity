package org.example.reactiveuniversity.security;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class TokenStore {
    private final Map<String, String> tokenMap = new HashMap<>();
    @Value("${token.store.path}")
    private String tokenStore;

    @PostConstruct
    void readInit() {
        read();
    }

    public void setToken(String email, String token) {
        tokenMap.put(email, token);
        writeToken(tokenMap);

    }

    public String getToken(String email) {
        read();
        return tokenMap.get(email);
    }

    private void writeToken(Map<String, String> token) {
        try (var file = new FileWriter(tokenStore); var buffer = new BufferedWriter(file)) {
            for (Map.Entry<String, String> entry : token.entrySet()) {
                buffer.write(entry.getKey() + " token-> " + entry.getValue());
                buffer.newLine();
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void read() {
        try (var file = new FileReader(tokenStore); var buffer = new BufferedReader(file)) {

            tokenMap.putAll(buffer.lines().map(line -> line.split(" token-> ", 2)).collect(Collectors.toMap(k -> k[0], k -> k[1])));


        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

}
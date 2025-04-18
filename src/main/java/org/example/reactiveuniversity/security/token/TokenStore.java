package org.example.reactiveuniversity.security.token;

import jakarta.annotation.PostConstruct;
import org.example.reactiveuniversity.exception.ReadWriteFileException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class TokenStore {
    private final ApplicationEventPublisher publisher;
    private final Map<String, String> tokenMap = new HashMap<>();
    @Value("${token.store.path}")
    private String tokenStore;

    public TokenStore(ApplicationEventPublisher publisher) {
        this.publisher = publisher;
    }

    @PostConstruct
    void initMap() {
        read();
    }


    public void setToken(String email, String token) {
        tokenMap.put(email, token);
        writeToken(tokenMap);

    }

    public String getToken(String email) {
        publisher.publishEvent(new TokenUpdatedEvent(this, tokenStore));
        return tokenMap.get(email);
    }

    private void writeToken(Map<String, String> token) {
        try (var file = new FileWriter(tokenStore); var buffer = new BufferedWriter(file)) {
            for (Map.Entry<String, String> entry : token.entrySet()) {
                buffer.write(entry.getKey() + " token-> " + entry.getValue());
                buffer.newLine();
            }

        } catch (IOException e) {
            throw new ReadWriteFileException("Write token file error");
        }
    }

    protected void read() {
        try (var file = new FileReader(tokenStore); var buffer = new BufferedReader(file)) {

            tokenMap.putAll(buffer.lines().map(line -> line.split(" token-> ", 2)).collect(Collectors.toMap(k -> k[0], k -> k[1])));


        } catch (IOException e) {
            throw new ReadWriteFileException("Read token file error");
        }

    }

}
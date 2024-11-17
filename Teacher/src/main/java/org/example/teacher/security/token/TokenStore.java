package org.example.teacher.security.token;


import org.example.teacher.exception.ReadWriteFileException;
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


    public String getToken(String email) {
        publisher.publishEvent(new TokenUpdatedEvent(this, tokenStore));
        return tokenMap.get(email);
    }

   

    protected void read() {
        try (var file = new FileReader(tokenStore);
             var buffer = new BufferedReader(file)) {

            tokenMap.putAll(buffer.lines().map(line -> line.split(" token-> ", 2)).collect(Collectors.toMap(k -> k[0], k -> k[1])));


        } catch (IOException e) {
            throw  new ReadWriteFileException("Read token file error");
        }

    }

}
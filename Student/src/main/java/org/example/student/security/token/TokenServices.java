package org.example.student.security.token;

import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class TokenServices {
    private final DatabaseClient databaseClient;

    public TokenServices(DatabaseClient databaseClient) {
        this.databaseClient = databaseClient;
    }

    public Mono<Token> findTokenByEmail(String email) {
        return databaseClient.sql("SELECT token FROM token WHERE email = ?").bind(0, email).map((row, metadata) -> new Token(row.get("token", String.class))).one();
    }
}

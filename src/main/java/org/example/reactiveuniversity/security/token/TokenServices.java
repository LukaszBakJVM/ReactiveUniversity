package org.example.reactiveuniversity.security.token;

import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class TokenServices {
    private final TokenRepository repository;
    private final TokenMapper tokenMapper;

    public TokenServices(TokenRepository repository, TokenMapper tokenMapper) {
        this.repository = repository;
        this.tokenMapper = tokenMapper;
    }



  public   Mono<String> getToken(String email) {
        return repository.findByEmail(email).map(tokenMapper::getToken).map(GetToken::token);
    }
}

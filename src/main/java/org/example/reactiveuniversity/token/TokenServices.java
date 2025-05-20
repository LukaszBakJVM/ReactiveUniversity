package org.example.reactiveuniversity.token;

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

    public Mono<TokenEntity> saveToken(String email, String token) {
        return repository.findByEmail(email).map(e -> {
                    e.setToken(token);
                    return e;
                }).flatMap(repository::save)

                .switchIfEmpty(repository.save(tokenMapper.saveToken(new TokenSaveDto(email, token))));

    }

  public   Mono<String> getToken(String email) {
        return repository.findByEmail(email).map(tokenMapper::getToken).map(GetToken::token);
    }
}

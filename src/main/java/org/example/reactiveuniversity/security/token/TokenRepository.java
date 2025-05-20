package org.example.reactiveuniversity.security.token;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface TokenRepository extends ReactiveCrudRepository<TokenEntity,Long> {
    Mono<TokenEntity>findByEmail(String email);
}

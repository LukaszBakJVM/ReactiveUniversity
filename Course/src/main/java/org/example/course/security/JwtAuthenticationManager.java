package org.example.course.security;

import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
class JwtAuthenticationManager implements ReactiveAuthenticationManager {

    private final JwtService jwtService;

    JwtAuthenticationManager(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {
        return Mono.just(authentication).cast(JwtToken.class).filter(jwtToken -> jwtService.isTokenValid(jwtToken.getToken())).map(jwtToken -> jwtToken.withAuthenticated(true)).switchIfEmpty(Mono.error(new JwtAuthenticationException("Invalid token.")));
    }
}

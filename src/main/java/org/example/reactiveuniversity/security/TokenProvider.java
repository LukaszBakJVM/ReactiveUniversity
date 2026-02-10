package org.example.reactiveuniversity.security;

import org.springframework.security.core.userdetails.UserDetails;
import reactor.core.publisher.Mono;

public interface TokenProvider {
   Mono<String> generateToken(UserDetails userDetails);
}

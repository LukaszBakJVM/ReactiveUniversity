package org.example.reactiveuniversity.security;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
public class LoginControler {
    private final CustomUserDetailsService services;

    public LoginControler(CustomUserDetailsService services) {
        this.services = services;
    }


    @PostMapping("/login")
    Mono<String> tokenMono(@RequestBody Login login) {
        return services.token(login);
    }


}

package org.example.reactiveuniversity.security;

import org.example.reactiveuniversity.dto.Token;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
public class RestLoginController {
    private final CustomUserDetailsService services;

    public RestLoginController(CustomUserDetailsService services) {
        this.services = services;
    }


    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    Mono<Token> token(@RequestBody Login login) {
        return services.token(login);
    }


}

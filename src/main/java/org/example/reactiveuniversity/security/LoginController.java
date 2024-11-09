package org.example.reactiveuniversity.security;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
public class LoginController {
    private final CustomUserDetailsService services;

    public LoginController(CustomUserDetailsService services) {
        this.services = services;
    }


    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    Mono<String> tokenMono(@RequestBody Login login) {
        return services.token(login);
    }


}

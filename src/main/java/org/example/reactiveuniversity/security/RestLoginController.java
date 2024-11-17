package org.example.reactiveuniversity.security;

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


    @PostMapping("/restLogin")
    @ResponseStatus(HttpStatus.OK)
    Mono<String> token(@RequestBody Login login) {
        return services.token(login);
    }


}

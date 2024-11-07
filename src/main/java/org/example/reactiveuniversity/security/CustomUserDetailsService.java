package org.example.reactiveuniversity.security;


import org.example.reactiveuniversity.RegistrationService;
import org.example.reactiveuniversity.exception.WrongCredentialsException;
import org.example.reactiveuniversity.security.tokenstore.TokenStore;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class CustomUserDetailsService implements ReactiveUserDetailsService {
    private final RegistrationService service;
    private final TokenProvider token;
    private final PasswordEncoder passwordEncoder;
    private final TokenStore tokenStore;


    public CustomUserDetailsService(RegistrationService service, TokenProvider token, PasswordEncoder passwordEncoder, TokenStore tokenStore) {
        this.service = service;
        this.token = token;
        this.passwordEncoder = passwordEncoder;
        this.tokenStore = tokenStore;
    }


    Mono<String> token(Login login) {
        return this.findByUsername(login.email()).filter(u -> passwordEncoder.matches(login.password(), u.getPassword())).map(user -> {
            String generatedToken = token.generateToken(user);
            tokenStore.setToken(login.email(), generatedToken);
            return generatedToken;
        }).switchIfEmpty(Mono.error(new WrongCredentialsException("Bad Credentials")));

    }


    @Override
    public Mono<UserDetails> findByUsername(String username) {
        return service.login(username).map(this::create);
    }

    UserDetails create(Login login) {
        return User.builder().username(login.email()).password(login.password()).roles(login.roles()).build();
    }
}

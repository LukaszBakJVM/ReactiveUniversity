package org.example.reactiveuniversity.security;


import org.example.reactiveuniversity.RegistrationService;
import org.example.reactiveuniversity.dto.Token;
import org.example.reactiveuniversity.token.TokenServices;
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

    private final TokenServices tokenServices;


    public CustomUserDetailsService(RegistrationService service, TokenProvider token, PasswordEncoder passwordEncoder, TokenServices tokenServices) {
        this.service = service;
        this.token = token;
        this.passwordEncoder = passwordEncoder;

        this.tokenServices = tokenServices;
    }


    Mono<Token> token(Login login) {
        return findByUsername(login.email()).filter(u -> passwordEncoder.matches(login.password(), u.getPassword())).map(token::generateToken).map(Token::new).flatMap(e -> tokenServices.saveToken(login.email(), e.token()).thenReturn(e).switchIfEmpty(Mono.error(new JwtAuthenticationException(""))));

    }


    @Override
    public Mono<UserDetails> findByUsername(String username) {
        return service.login(username).map(this::create);
    }

    private UserDetails create(Login login) {
        return User.builder().username(login.email()).password(login.password()).roles(login.roles()).build();
    }
}

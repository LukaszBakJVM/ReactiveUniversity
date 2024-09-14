package org.example.reactiveuniversity.security;


import org.example.reactiveuniversity.dto.Login;
import org.example.reactiveuniversity.exception.UsernameNotFoundException;
import org.example.reactiveuniversity.RegistrationService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final RegistrationService registrationService;

    public CustomUserDetailsService(RegistrationService registrationService) {
        this.registrationService = registrationService;
    }


    @Override
    public UserDetails loadUserByUsername(String username) {
        return registrationService.login(username).map(this::createUserDetails).orElseThrow(() -> new UsernameNotFoundException(String.format("User with email %s not found", username)));

    }

    private UserDetails createUserDetails(Login login) {
        return User.builder().username(login.email()).password(login.password()).roles(login.role()).build();
    }


}

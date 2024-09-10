package org.example.office.security;

import org.example.office.OfficeServices;
import org.example.office.dto.OfficeLogin;
import org.example.office.exception.UsernameNotFoundException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final OfficeServices officeServices;

    public CustomUserDetailsService(OfficeServices officeServices) {
        this.officeServices = officeServices;
    }


    @Override
    public UserDetails loadUserByUsername(String username) {
        return officeServices.login(username).map(this::createUserDetails).switchIfEmpty(Mono.error(new UsernameNotFoundException(String.format("User with email %s not found", username)))).block();

    }

    private UserDetails createUserDetails(OfficeLogin login) {
        return User.builder().username(login.email()).password(login.password()).roles(login.role()).build();
    }


}

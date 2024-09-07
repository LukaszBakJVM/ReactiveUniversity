package org.example.teacher.security;

import org.example.teacher.TeacherServices;
import org.example.teacher.dto.TeacherLogin;
import org.example.teacher.exception.UsernameNotFoundException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final TeacherServices teacherServices;

    public CustomUserDetailsService(TeacherServices teacherServices) {
        this.teacherServices = teacherServices;
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        return teacherServices.login(username).map(
                this::createUserDetails).switchIfEmpty(Mono.error( new UsernameNotFoundException(String.format("User with email %s not found", username)))).block();

    }

    private UserDetails createUserDetails(TeacherLogin login) {
        return User.builder()
                .username(login.email())
                .password(login.password())
                .roles(login.role())
                .build();
    }


}

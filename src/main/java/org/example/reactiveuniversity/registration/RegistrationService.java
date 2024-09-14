package org.example.reactiveuniversity.registration;

import jakarta.validation.ConstraintViolation;
import org.example.reactiveuniversity.dto.Login;
import org.example.reactiveuniversity.dto.RegistrationResponseDto;
import org.example.reactiveuniversity.dto.Teacher;
import org.example.reactiveuniversity.exception.CustomValidationException;
import org.example.reactiveuniversity.exception.DuplicateEmailException;
import org.example.reactiveuniversity.security.TokenStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class RegistrationService {
    private final RegistrationRepository registrationRepository;
    private final RegistrationMapper registrationMapper;
    private final LocalValidatorFactoryBean validation;
    private final WebClient.Builder webClientBuilder;
    private final TokenStore tokenStore;
    @Value("${teacher}")
    private String teacher;
    @Value("${course}")
    private String courseUrl;
    @Value("${student}")
    private String studentUrl;
    @Value("${subject}")
    private String subjectUrl;


    public RegistrationService(RegistrationRepository registrationRepository, RegistrationMapper registrationMapper, LocalValidatorFactoryBean validation, WebClient.Builder webClientBuilder, TokenStore tokenStore) {
        this.registrationRepository = registrationRepository;
        this.registrationMapper = registrationMapper;
        this.validation = validation;
        this.webClientBuilder = webClientBuilder;
        this.tokenStore = tokenStore;
    }

    List<String> role() {

        return Arrays.stream(Role.values()).map(Role::getROLE).toList();
    }

    RegistrationResponseDto createNewUser(RegistrationDto registrationDto) {
        Optional<Registration> byEmail = registrationRepository.findByEmail(registrationDto.email());
        byEmail.ifPresent(present -> {
            throw new DuplicateEmailException("Email exist");
        });
        Registration registration = registrationMapper.dtoToEntity(registrationDto);
        validationRegistration(registration);
        Registration save = registrationRepository.save(registration);
        return registrationMapper.entityToDto(save);
    }

    public Optional<Login> login(String email) {
        return registrationRepository.findByEmail(email).map(registrationMapper::login);
    }

    Mono<Teacher> email(String email) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String name = authentication.getName();
        String token = tokenStore.getToken(name);

        return webClientBuilder.baseUrl(teacher).build().get().uri("/teacher/{email}", email).header(HttpHeaders.AUTHORIZATION, "Bearer " + token).retrieve().bodyToMono(Teacher.class);
    }


    private void validationRegistration(Registration registration) {
        Set<ConstraintViolation<Registration>> violations = validation.validate(registration);

        if (!violations.isEmpty()) {
            String errorMessage = violations.stream().map(ConstraintViolation::getMessage).collect(Collectors.joining(" , "));

            throw new CustomValidationException(errorMessage);
        }
    }
}

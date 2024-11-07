package org.example.reactiveuniversity;

import jakarta.validation.ConstraintViolation;
import org.example.reactiveuniversity.dto.RegistrationDto;
import org.example.reactiveuniversity.dto.RegistrationResponseDto;
import org.example.reactiveuniversity.dto.WriteNewPerson;
import org.example.reactiveuniversity.exception.*;
import org.example.reactiveuniversity.security.Login;
import org.example.reactiveuniversity.security.tokenstore.TokenStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import reactor.core.publisher.Mono;

import java.security.Principal;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class RegistrationService {
    private final RegistrationRepository registrationRepository;
    private final RegistrationMapper registrationMapper;
    private final LocalValidatorFactoryBean validation;
    private final TokenStore tokenStore;
    private final WebClient.Builder webclient;
    @Value("${teacher}")
    private String teacherUrl;
    @Value("${student}")
    private String studentUrl;
    @Value("${office}")
    private String officeUrl;


    public RegistrationService(RegistrationRepository registrationRepository, RegistrationMapper registrationMapper, LocalValidatorFactoryBean validation, TokenStore tokenStore, WebClient.Builder webclient) {
        this.registrationRepository = registrationRepository;
        this.registrationMapper = registrationMapper;
        this.validation = validation;
        this.tokenStore = tokenStore;
        this.webclient = webclient;
    }

    List<String> role() {
        return Arrays.stream(Role.values()).map(Role::getROLE).toList();
    }

    @Transactional
    public Mono<RegistrationResponseDto> createNewUser(RegistrationDto registrationDto) {
        return registrationRepository.findByEmailIgnoreCase(registrationDto.email()).flatMap(existingSubject -> Mono.<RegistrationResponseDto>error(new DuplicateEmailException(String.format("Email %s already exists", registrationDto.email())))).switchIfEmpty(Mono.defer(() -> {

            Registration registration = registrationMapper.dtoToEntity(registrationDto);
            validationRegistration(registration);
            WriteNewPerson write = registrationMapper.write(registration);
            Mono<String> name = ReactiveSecurityContextHolder.getContext().map(SecurityContext::getAuthentication).map(Principal::getName);


            return name.flatMap(e -> writeUser(registrationDto.role(), write, tokenStore.getToken(e)).then(registrationRepository.save(registration).map(registrationMapper::entityToDto)));
        }));
    }

    public Mono<Login> login(String email) {
        return registrationRepository.findByEmail(email).map(registrationMapper::login);
    }


    private void validationRegistration(Registration registration) {
        Set<ConstraintViolation<Registration>> violations = validation.validate(registration);

        if (!violations.isEmpty()) {
            String errorMessage = violations.stream().map(ConstraintViolation::getMessage).collect(Collectors.joining(" , "));

            throw new CustomValidationException(errorMessage);
        }
    }


    private Mono<Void> writeUser(String role, WriteNewPerson body, String token) {
        String authorization = "Authorization";
        String header = "Bearer %s".formatted(token);
        String url = switch (role) {
            case "Office" -> officeUrl + "/office";
            case "Teacher" -> teacherUrl + "/teacher";
            case "Student" -> studentUrl + "/student";
            default -> throw new WrongRoleException("Unknown Error");
        };

        return webclient.baseUrl(url).build().post().header(authorization, header).accept(MediaType.APPLICATION_JSON).bodyValue(body).retrieve().onStatus(HttpStatusCode::is4xxClientError, response -> Mono.error(new WrongCredentialsException("Wrong credentials"))).bodyToMono(Void.class).onErrorResume(WebClientRequestException.class, response -> Mono.error(new ConnectionException("Connection Error")));
    }
}




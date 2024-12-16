package org.example.reactiveuniversity;

import jakarta.validation.ConstraintViolation;
import org.example.reactiveuniversity.appconfig.KafkaProducerServices;
import org.example.reactiveuniversity.dto.RegistrationDto;
import org.example.reactiveuniversity.dto.RegistrationResponseDto;
import org.example.reactiveuniversity.dto.WriteNewPerson;
import org.example.reactiveuniversity.exception.CustomValidationException;
import org.example.reactiveuniversity.exception.DuplicateEmailException;
import org.example.reactiveuniversity.security.Login;
import org.example.reactiveuniversity.security.token.TokenStore;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
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

    private final KafkaProducerServices kafkaProducerService;


    public RegistrationService(RegistrationRepository registrationRepository, RegistrationMapper registrationMapper, LocalValidatorFactoryBean validation, TokenStore tokenStore, KafkaProducerServices kafkaProducerService) {
        this.registrationRepository = registrationRepository;
        this.registrationMapper = registrationMapper;
        this.validation = validation;
        this.tokenStore = tokenStore;

        this.kafkaProducerService = kafkaProducerService;
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


    public Mono<Void> writeUser(String role, WriteNewPerson body, String token) {
        return Mono.defer(() -> {
            switch (role) {
                case "Office":
                    return kafkaProducerService.sendMessage("office-topic", body, token); // Wysyłanie wiadomości do "office-topic"
                case "Teacher":
                    return kafkaProducerService.sendMessage("teacher-topic", body, token); // Wysyłanie wiadomości do "teacher-topic"
                case "Student":
                    return kafkaProducerService.sendMessage("student-topic", body, token); // Wysyłanie wiadomości do "student-topic"
                default:
                    return Mono.error(new CustomValidationException("Invalid role"));
            }
        });
    }
}








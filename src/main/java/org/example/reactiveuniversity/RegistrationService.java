package org.example.reactiveuniversity;

import jakarta.validation.ConstraintViolation;
import org.example.reactiveuniversity.dto.RegistrationDto;
import org.example.reactiveuniversity.dto.RegistrationResponseDto;
import org.example.reactiveuniversity.dto.WriteNewPerson;
import org.example.reactiveuniversity.exception.ConnectionException;
import org.example.reactiveuniversity.exception.CustomValidationException;
import org.example.reactiveuniversity.exception.DuplicateEmailException;
import org.example.reactiveuniversity.security.Login;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class RegistrationService {
    private final RegistrationRepository registrationRepository;
    private final RegistrationMapper registrationMapper;
    private final LocalValidatorFactoryBean validation;
    private final KafkaServices kafkaService;


    public RegistrationService(RegistrationRepository registrationRepository, RegistrationMapper registrationMapper, LocalValidatorFactoryBean validation, KafkaServices kafkaService) {
        this.registrationRepository = registrationRepository;
        this.registrationMapper = registrationMapper;
        this.validation = validation;
        this.kafkaService = kafkaService;
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
             writeUser(registration.getRole(), write).subscribe();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            WriteNewPerson writeNewPerson = kafkaService.writeNewPerson;
            if (!writeNewPerson.equals(write)) {
                    return Mono.error(new ConnectionException("Connection error"));
                }
                return registrationRepository.save(registration).map(registrationMapper::entityToDto);
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


    public Mono<Void> writeUser(String role, WriteNewPerson body) {
        return Mono.defer(() -> switch (role) {
            case "Office" -> kafkaService.sendMessage("office-topic", body);
            case "Teacher" -> kafkaService.sendMessage("teacher-topic", body);
            case "Student" -> kafkaService.sendMessage("student-topic", body);
            default -> Mono.error(new CustomValidationException("Invalid role"));
        });
    }
}








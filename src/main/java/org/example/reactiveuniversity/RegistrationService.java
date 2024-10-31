package org.example.reactiveuniversity;

import jakarta.validation.ConstraintViolation;
import org.example.reactiveuniversity.dto.RegistrationDto;
import org.example.reactiveuniversity.dto.RegistrationResponseDto;
import org.example.reactiveuniversity.dto.WriteNewPerson;
import org.example.reactiveuniversity.exception.CustomValidationException;
import org.example.reactiveuniversity.exception.DuplicateEmailException;
import org.example.reactiveuniversity.exception.WrongRoleException;
import org.example.reactiveuniversity.security.Login;
import org.example.reactiveuniversity.security.TokenStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.client.RestTemplate;
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
    private final RestTemplate restTemplate;
    private final TokenStore tokenStore;
    @Value("${teacher}")
    private String teacherUrl;
    @Value("${student}")
    private String studentUrl;
    @Value("${office}")
    private String officeUrl;


    public RegistrationService(RegistrationRepository registrationRepository, RegistrationMapper registrationMapper, LocalValidatorFactoryBean validation, RestTemplate restTemplate, TokenStore tokenStore) {
        this.registrationRepository = registrationRepository;
        this.registrationMapper = registrationMapper;
        this.validation = validation;
        this.restTemplate = restTemplate;

        this.tokenStore = tokenStore;
    }

    List<String> role() {
        return Arrays.stream(Role.values()).map(Role::getROLE).toList();
    }

    @Transactional
    public Mono<RegistrationResponseDto> createNewUser(RegistrationDto registrationDto, String email) {
        return registrationRepository.findByEmail(registrationDto.email()).flatMap(existingSubject -> Mono.<RegistrationResponseDto>error(new DuplicateEmailException(String.format("Email %s already exists", registrationDto.email())))).switchIfEmpty(Mono.just(registrationMapper.dtoToEntity(registrationDto)).flatMap(entity -> {

            validationRegistration(entity);
            WriteNewPerson write = registrationMapper.write(entity);
            String token = tokenStore.getToken(email);


            writeUser(registrationDto.role(), write, token);


            return Mono.just(registrationMapper.entityToDto(entity));
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


    private void writeUser(String role, WriteNewPerson body, String token) {
        String authorization = "Authorization";
        String header = "Bearer %s".formatted(token);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set(authorization, header);

        HttpEntity<WriteNewPerson> request = new HttpEntity<>(body, headers);
        switch (role) {
            case "Office":
                restTemplate.exchange(officeUrl + "/office", HttpMethod.POST, request, WriteNewPerson.class);
                break;
            case "Teacher":
                restTemplate.exchange(teacherUrl + "/teacher", HttpMethod.POST, request, WriteNewPerson.class);
                break;
            case "Student":
                restTemplate.exchange(studentUrl + "/student", HttpMethod.POST, request, WriteNewPerson.class);
                break;
            default:
                throw new WrongRoleException("Unknown Error");


        }

    }
}




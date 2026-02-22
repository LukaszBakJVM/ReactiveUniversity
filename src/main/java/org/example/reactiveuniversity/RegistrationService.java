package org.example.reactiveuniversity;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.validation.ConstraintViolation;
import org.example.reactiveuniversity.dto.RegistrationDto;
import org.example.reactiveuniversity.dto.RegistrationResponseDto;
import org.example.reactiveuniversity.dto.UserInfo;
import org.example.reactiveuniversity.dto.WriteNewPerson;
import org.example.reactiveuniversity.exception.*;
import org.example.reactiveuniversity.security.Login;
import org.example.reactiveuniversity.security.token.TokenServices;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import reactor.core.publisher.Mono;

import javax.crypto.SecretKey;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class RegistrationService {
    private final RegistrationRepository registrationRepository;
    private final RegistrationMapper registrationMapper;
    private final LocalValidatorFactoryBean validation;
    private final TokenServices tokenServices;


    private final WebClient webclient;
    @Value("${teacher}")
    private String teacherUrl;
    @Value("${student}")
    private String studentUrl;
    @Value("${office}")
    private String officeUrl;

    @Value("${jws.sharedKey}")
    private String sharedKey;


    public RegistrationService(RegistrationRepository registrationRepository, RegistrationMapper registrationMapper, LocalValidatorFactoryBean validation, TokenServices tokenServices, WebClient webclient) {
        this.registrationRepository = registrationRepository;
        this.registrationMapper = registrationMapper;
        this.validation = validation;
        this.tokenServices = tokenServices;

        this.webclient = webclient;
    }

    List<String> role() {
        return Arrays.stream(Role.values()).map(Role::getROLE).toList();
    }


    public Mono<RegistrationResponseDto> createNewUser(RegistrationDto registrationDto) {


        return registrationRepository.findByEmailIgnoreCase(registrationDto.email()).flatMap(existingSubject -> Mono.<RegistrationResponseDto>error(new DuplicateEmailException(String.format("Email %s already exists", registrationDto.email())))).switchIfEmpty(Mono.defer(() -> {

            Registration registration = registrationMapper.dtoToEntity(registrationDto);
            validationRegistration(registration);


            return writeUser(registrationDto.role(), new WriteNewPerson(registrationDto.firstName(), registrationDto.firstName(), registrationDto.email())).then(registrationRepository.save(registration).map(registrationMapper::entityToDto));
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

    Mono<UserInfo> userInfo(String token) {

        if (token.equals("notLogged")) {
            return Mono.empty();
        }

        Claims body = Jwts.parser().setSigningKey(getSigningKey()).build().parseClaimsJws(token).getBody();


        String email = body.get("email", String.class);
        List<String> role = body.get("roles", List.class);


        return Mono.just(new UserInfo(email, role.getFirst()));
    }

    public Mono<Long> userId(String email) {
        return registrationRepository.findByEmail(email).map(Registration::getId);
    }


    private SecretKey getSigningKey() {
        byte[] keyBytes = io.jsonwebtoken.io.Decoders.BASE64.decode(sharedKey);
        return io.jsonwebtoken.security.Keys.hmacShaKeyFor(keyBytes);


    }


    private Mono<Void> writeUser(String role, WriteNewPerson body) {

        String url = switch (role) {
            case "Office" -> officeUrl + "/office";
            case "Teacher" -> teacherUrl + "/teacher";
            case "Student" -> studentUrl + "/student";
            default -> throw new WrongRoleException("Unknown Error");
        };


        return webclient.post().uri(url).accept(MediaType.APPLICATION_JSON).bodyValue(body).retrieve().onStatus(HttpStatusCode::is4xxClientError, response -> Mono.error(new WrongCredentialsException("Wrong credentials"))).bodyToMono(Void.class).onErrorResume(WebClientRequestException.class, response -> Mono.error(new ConnectionException("Connection Error")));
    }


}




package org.example.reactiveuniversity;

import jakarta.validation.ConstraintViolation;
import org.example.reactiveuniversity.dto.Login;
import org.example.reactiveuniversity.dto.RegistrationDto;
import org.example.reactiveuniversity.dto.RegistrationResponseDto;
import org.example.reactiveuniversity.exception.CustomValidationException;
import org.example.reactiveuniversity.exception.DuplicateEmailException;
import org.example.reactiveuniversity.exception.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

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
    private final PasswordEncoder passwordEncoder;


    public RegistrationService(RegistrationRepository registrationRepository, RegistrationMapper registrationMapper, LocalValidatorFactoryBean validation, PasswordEncoder passwordEncoder) {
        this.registrationRepository = registrationRepository;
        this.registrationMapper = registrationMapper;
        this.validation = validation;
        this.passwordEncoder = passwordEncoder;
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

    void changePassword(String email, String password) {
        Registration user = registrationRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        String newPassword = passwordEncoder.encode(password);
        user.setPassword(newPassword);
        registrationRepository.save(user);

    }


    private void validationRegistration(Registration registration) {
        Set<ConstraintViolation<Registration>> violations = validation.validate(registration);

        if (!violations.isEmpty()) {
            String errorMessage = violations.stream().map(ConstraintViolation::getMessage).collect(Collectors.joining(" , "));

            throw new CustomValidationException(errorMessage);
        }
    }
}

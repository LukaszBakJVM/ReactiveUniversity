package org.example.reactiveuniversity;

import org.example.reactiveuniversity.dto.RegistrationDto;
import org.example.reactiveuniversity.dto.RegistrationResponseDto;
import org.example.reactiveuniversity.dto.WriteNewPerson;
import org.example.reactiveuniversity.security.Login;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class RegistrationMapper {
    private final PasswordEncoder passwordEncoder;

    public RegistrationMapper(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    Registration dtoToEntity(RegistrationDto dto) {
        Registration registration = new Registration();
        registration.setFirstName(dto.firstName());
        registration.setLastName(dto.lastName());
        registration.setEmail(dto.email());
        String password = passwordEncoder.encode(dto.password());
        registration.setPassword(password);
        registration.setRole(dto.role());
        return registration;
    }

    RegistrationResponseDto entityToDto(Registration registration) {
        return new RegistrationResponseDto(registration.getFirstName(), registration.getLastName(), registration.getEmail(), registration.getRole());
    }

    Login login(Registration registration) {
        return new Login(registration.getEmail(), registration.getPassword(), registration.getRole());
    }

    WriteNewPerson write(Registration registration) {
        return new WriteNewPerson(registration.getFirstName(), registration.getLastName(), registration.getEmail());
    }


}

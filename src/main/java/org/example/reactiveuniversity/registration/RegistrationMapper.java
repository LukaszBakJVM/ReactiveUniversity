package org.example.reactiveuniversity.registration;

import org.example.reactiveuniversity.dto.RegistrationResponseDto;
import org.springframework.stereotype.Service;

@Service
public class RegistrationMapper {

    Registration dtoToEntity(RegistrationDto dto) {
        Registration registration = new Registration();
        registration.setFirstName(dto.firstName());
        registration.setLastName(dto.lastName());
        registration.setEmail(dto.email());
        registration.setPassword(dto.password());
        registration.setRole(dto.role());
        return registration;
    }
    RegistrationResponseDto entityToDto(Registration registration){
        return new RegistrationResponseDto(registration.getFirstName(),registration.getLastName(), registration.getEmail(),registration.getRole());
    }


}

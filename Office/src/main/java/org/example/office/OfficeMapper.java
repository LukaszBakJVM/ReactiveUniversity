package org.example.office;

import org.example.office.dto.CreateNewPersonOffice;
import org.example.office.dto.CreateNewPersonOfficeResponse;
import org.example.office.dto.OfficeLogin;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class OfficeMapper {
    private final String ROLE = "Office";
    private final PasswordEncoder passwordEncoder;

    public OfficeMapper(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    Office dtoToOffice(CreateNewPersonOffice dto) {
        Office office = new Office();
        office.setFirstName(dto.firstName());
        office.setLastName(dto.lastName());
        office.setEmail(dto.email());
        String password = passwordEncoder.encode(dto.password());
        office.setPassword(password);
        office.setRole(ROLE);
        return office;
    }

    CreateNewPersonOfficeResponse officeToDto(Office office) {
        return new CreateNewPersonOfficeResponse(office.getFirstName(), office.getLastName(), office.getEmail());
    }

    OfficeLogin login(Office office) {
        return new OfficeLogin(office.getEmail(), office.getPassword(), office.getRole());
    }
}

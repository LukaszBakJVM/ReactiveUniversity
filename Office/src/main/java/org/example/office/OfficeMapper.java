package org.example.office;

import org.example.office.dto.CreateNewPersonOffice;
import org.example.office.dto.CreateNewPersonOfficeResponse;
import org.springframework.stereotype.Service;

@Service
public class OfficeMapper {
    private final String ROLE = "Office";

    Office dtoToOffice(CreateNewPersonOffice dto) {
        Office office = new Office();
        office.setFirstName(dto.firstNme());
        office.setLastName(dto.lastName());
        office.setEmail(dto.email());
        office.setPassword(dto.password());
        office.setRole(ROLE);
        return office;
    }
    CreateNewPersonOfficeResponse officeToDto(Office office){
        return new CreateNewPersonOfficeResponse(office.getFirstName(),office.getLastName(),office.getEmail());
    }
}

package org.example.office;

import org.example.office.dto.WriteNewPersonOffice;
import org.springframework.stereotype.Service;

@Service
public class OfficeMapper {


    Office dtoToOffice(WriteNewPersonOffice dto) {
        Office office = new Office();
        office.setFirstName(dto.firstName());
        office.setLastName(dto.lastName());
        office.setEmail(dto.email());
        return office;
    }

    WriteNewPersonOffice officeToDto(Office office) {
        return new WriteNewPersonOffice(office.getFirstName(), office.getLastName(), office.getEmail());
    }


}

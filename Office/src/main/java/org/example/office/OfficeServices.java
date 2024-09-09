package org.example.office;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ValidationException;
import org.example.office.dto.CreateNewPersonOffice;
import org.example.office.dto.CreateNewPersonOfficeResponse;
import org.springframework.stereotype.Service;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import reactor.core.publisher.Mono;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class OfficeServices {
    private final LocalValidatorFactoryBean validation;

    private final OfficeRepository officeRepository;
    private final OfficeMapper officeMapper;

    public OfficeServices(LocalValidatorFactoryBean validation, OfficeRepository officeRepository, OfficeMapper officeMapper) {
        this.validation = validation;
        this.officeRepository = officeRepository;
        this.officeMapper = officeMapper;
    }

    Mono<CreateNewPersonOfficeResponse> createNewPerson(CreateNewPersonOffice dto) {
        Office office = officeMapper.dtoToOffice(dto);
        validationOfficePerson(office);
        return officeRepository.save(office).map(officeMapper::officeToDto);
    }


    private void validationOfficePerson(Office office) {
        Set<ConstraintViolation<Office>> violations = validation.validate(office);

        if (!violations.isEmpty()) {
            String errorMessage = violations.stream().map(ConstraintViolation::getMessage).collect(Collectors.joining(" , "));

            throw new ValidationException(errorMessage);
        }
    }
}

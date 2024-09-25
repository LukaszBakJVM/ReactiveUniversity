package org.example.office;

import jakarta.validation.ConstraintViolation;
import org.example.office.dto.CreateNewPersonOffice;
import org.example.office.dto.CreateNewPersonOfficeResponse;
import org.example.office.dto.Teacher;
import org.example.office.exception.CustomValidationException;
import org.example.office.exception.DuplicateEmailException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class OfficeServices {

    private final LocalValidatorFactoryBean validation;
    private final OfficeRepository officeRepository;
    private final OfficeMapper officeMapper;
    private final WebClient.Builder webClientBuilder;
    @Value("${teacher}")
    private String teacher;
    @Value("${course}")
    private String courseUrl;
    @Value("${student}")
    private String studentUrl;

    public OfficeServices(LocalValidatorFactoryBean validation, OfficeRepository officeRepository, OfficeMapper officeMapper, WebClient.Builder webClientBuilder) {
        this.validation = validation;
        this.officeRepository = officeRepository;
        this.officeMapper = officeMapper;
        this.webClientBuilder = webClientBuilder;
    }

    Mono<CreateNewPersonOfficeResponse> createNewPerson(CreateNewPersonOffice dto) {
        Office office = officeMapper.dtoToOffice(dto);
        validationOfficePerson(office);
        return officeRepository.findByEmail(dto.email()).flatMap(existingEmail -> Mono.<CreateNewPersonOfficeResponse>error(new DuplicateEmailException(String.format("Email %s already exists", dto.email())))).switchIfEmpty(officeRepository.save(officeMapper.dtoToOffice(dto)).map(officeMapper::officeToDto));
    }


    Mono<Teacher> byEmail(String email, String token) {
        return webClientBuilder.baseUrl(teacher).build().get().uri("teacher/{email}", email).header(HttpHeaders.AUTHORIZATION, "Bearer " + token).retrieve().bodyToMono(Teacher.class);
    }


    private void validationOfficePerson(Office office) {
        Set<ConstraintViolation<Office>> violations = validation.validate(office);

        if (!violations.isEmpty()) {
            String errorMessage = violations.stream().map(ConstraintViolation::getMessage).collect(Collectors.joining(" , "));

            throw new CustomValidationException(errorMessage);
        }
    }
}

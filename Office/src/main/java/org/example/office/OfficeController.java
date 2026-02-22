package org.example.office;


import org.example.office.dto.WriteNewPersonOffice;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/office")
public class OfficeController {
    private final OfficeServices officeServices;

    public OfficeController(OfficeServices officeServices) {
        this.officeServices = officeServices;
    }

    @PreAuthorize("hasRole('Office')")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    Mono<WriteNewPersonOffice> createNewPerson(@RequestBody WriteNewPersonOffice dto) {
        return officeServices.createNewPerson(dto);

    }


}

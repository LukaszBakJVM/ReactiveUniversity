package org.example.office;


import org.example.office.dto.WriteNewPersonOffice;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.net.URI;

@RestController
@RequestMapping("/office")
public class OfficeController {
    private final OfficeServices officeServices;

    public OfficeController(OfficeServices officeServices) {
        this.officeServices = officeServices;
    }

    @PostMapping
    Mono<ResponseEntity<WriteNewPersonOffice>> createNewTeacher(@RequestBody WriteNewPersonOffice dto) {
        return officeServices.createNewPerson(dto).map(office -> ResponseEntity.created(URI.create("/office")).body(office));
    }


}

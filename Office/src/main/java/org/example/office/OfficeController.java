package org.example.office;

import org.example.office.dto.Teacher;
import org.example.office.dto.WriteNewPersonOffice;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
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

    @GetMapping("/{email}")
    Mono<Teacher> findByEmail(@PathVariable String email) {
        Authentication username = SecurityContextHolder.getContext().getAuthentication();
        return officeServices.byEmail(email, username.getName());
    }


}

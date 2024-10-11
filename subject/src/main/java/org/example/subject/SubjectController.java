package org.example.subject;

import org.example.subject.dto.SubjectDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;

@RestController
@RequestMapping("/subject")
public class SubjectController {
    private final SubjectServices subjectServices;

    public SubjectController(SubjectServices subjectServices) {
        this.subjectServices = subjectServices;
    }

    @PostMapping
    Mono<ResponseEntity<SubjectDto>> addNewSubject(@RequestBody SubjectDto dto) {
        return subjectServices.createSubject(dto).map(subject -> ResponseEntity.created(URI.create("/subject")).body(subject));
    }
    @GetMapping("/all")
    Flux<SubjectDto>allSubject(){
        return subjectServices.findAllSubject();
    }


}

package org.example.subject;

import org.example.subject.dto.SubjectDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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


}

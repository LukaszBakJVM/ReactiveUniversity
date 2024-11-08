package org.example.subject;

import org.example.subject.dto.SubjectDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/subject")
public class SubjectController {
    private final SubjectServices subjectServices;

    public SubjectController(SubjectServices subjectServices) {
        this.subjectServices = subjectServices;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    Mono<SubjectDto> addNewSubject(@RequestBody SubjectDto dto) {
        return subjectServices.createSubject(dto);
    }

    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    Flux<SubjectDto> allSubject() {
        return subjectServices.findAllSubject();
    }

    @DeleteMapping("/{subjectName}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    Mono<ResponseEntity<Void>> deleteSubjectByName(@PathVariable String subjectName) {
        return subjectServices.deleteSubject(subjectName).then(Mono.just(ResponseEntity.noContent().build()));
    }


}

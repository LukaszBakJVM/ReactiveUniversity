package org.example.subject;

import org.example.subject.dto.Course;
import org.example.subject.dto.SubjectDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.List;

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

    // @GetMapping("/all")
    // Flux<SubjectDto>allSubject(){
    //   return subjectServices.findAll();
    //  }
    @GetMapping("/{subject}")
    Mono<List<Course>> findSubject(@PathVariable String subject) {
        return subjectServices.findBySubject(subject);
    }
}

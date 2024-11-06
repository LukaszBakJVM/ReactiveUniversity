package org.example.student.grades;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface GradesRepository extends ReactiveCrudRepository<Grades,String> {
    Mono<Grades>findByEmailAndSubject(String email,String subject);
    Flux<Grades>findByEmail(String email);
}

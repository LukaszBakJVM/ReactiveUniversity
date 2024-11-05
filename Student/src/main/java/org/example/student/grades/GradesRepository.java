package org.example.student.grades;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface GradesRepository extends ReactiveCrudRepository<Grades,String> {
    Mono<Grades>findByEmailAndAndSubject(String email,String subject);
}

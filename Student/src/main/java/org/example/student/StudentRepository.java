package org.example.student;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface StudentRepository extends ReactiveCrudRepository<Student, Long> {
    Mono<Student> findByEmail(String email);

    Flux<Student> findByEmailContaining(String email);
    Flux<Student>findByCourse(String course);

}

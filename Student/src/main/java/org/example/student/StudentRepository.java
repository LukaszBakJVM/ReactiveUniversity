package org.example.student;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

import java.util.List;

public interface StudentRepository extends ReactiveCrudRepository<Student, Long> {
    Mono<Student> findByEmail(String email);
    Mono<List<Student>>findByEmailContaining(String email);
}

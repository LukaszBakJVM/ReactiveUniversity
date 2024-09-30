package org.example.teacher;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface TeacherRepository extends ReactiveCrudRepository<Teacher, Long> {
    Mono<Teacher> findByEmail(String email);


}

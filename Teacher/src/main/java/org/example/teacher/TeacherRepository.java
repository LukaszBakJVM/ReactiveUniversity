package org.example.teacher;

import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Mono;

public interface TeacherRepository extends R2dbcRepository<Teacher, Long> {
    Mono<Teacher> findByEmail(String email);
}

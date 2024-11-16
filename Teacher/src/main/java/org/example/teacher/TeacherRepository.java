package org.example.teacher;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface TeacherRepository extends ReactiveCrudRepository<Teacher, Long> {
    Mono<Teacher> findByEmail(String email);

    @Query("SELECT * FROM teacher WHERE $1 = ANY(subject_name)")
    Mono<Teacher> findTeacherBySubjectNameContains(String subjectName);


}

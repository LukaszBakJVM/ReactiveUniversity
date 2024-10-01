package org.example.course;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;



public interface CourseRepository extends ReactiveCrudRepository<Course, Long> {
    Mono<Course> findByCourseName(String courseName);

    Mono<Void> deleteByCourseName(String courseName);

    @Query("SELECT * FROM course WHERE $1 = ANY(subject_name)")
    Flux<Course> findCourseBySubjectNameContaining(String subjectName);


}

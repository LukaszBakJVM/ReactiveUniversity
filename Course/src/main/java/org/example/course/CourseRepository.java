package org.example.course;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CourseRepository extends ReactiveCrudRepository<Course, Long> {
    Mono<Course> findByCourseName(String courseName);

    Mono<Void> deleteByCourseName(String courseName);

    Flux<Course> findCourseBySubjectName(String subjectName);


}

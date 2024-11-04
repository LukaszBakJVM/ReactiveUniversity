package org.example.student.grades;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface GradesRepository extends ReactiveCrudRepository<Grades,String> {
}

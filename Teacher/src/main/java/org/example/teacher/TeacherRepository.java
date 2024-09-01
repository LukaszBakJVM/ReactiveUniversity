package org.example.teacher;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface TeacherRepository extends ReactiveCrudRepository<Teacher,Long> {
}

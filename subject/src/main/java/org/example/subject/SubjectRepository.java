package org.example.subject;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface SubjectRepository extends ReactiveCrudRepository<Subject, Long> {

    Mono<Subject> findBySubject(String subject);

    Mono<Void> deleteBySubject(String subjectName);


}

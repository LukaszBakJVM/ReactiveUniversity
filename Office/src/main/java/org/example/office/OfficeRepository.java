package org.example.office;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface OfficeRepository extends ReactiveCrudRepository<Office, Long> {
    Mono<Office>findByEmail(String email);

}

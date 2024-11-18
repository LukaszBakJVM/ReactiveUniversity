package org.example.reactiveuniversity;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface RegistrationRepository extends ReactiveCrudRepository<Registration, Long> {
    Mono<Registration> findByEmailIgnoreCase(String email);

    Mono<Registration> findByEmail(String email);
}

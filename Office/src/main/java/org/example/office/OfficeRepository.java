package org.example.office;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface OfficeRepository extends ReactiveCrudRepository<Office,Long> {
}

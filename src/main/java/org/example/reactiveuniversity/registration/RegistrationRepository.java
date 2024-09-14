package org.example.reactiveuniversity.registration;

import org.springframework.data.repository.CrudRepository;

import java.util.Optional;


public interface RegistrationRepository extends CrudRepository<Registration,Long> {
    Optional<Registration> findByEmail(String email);
}

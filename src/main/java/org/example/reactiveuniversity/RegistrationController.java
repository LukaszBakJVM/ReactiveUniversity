package org.example.reactiveuniversity;

import org.example.reactiveuniversity.dto.RegistrationDto;
import org.example.reactiveuniversity.dto.RegistrationResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/user")

public class RegistrationController {
    private final RegistrationService registrationService;

    public RegistrationController(RegistrationService registrationService) {
        this.registrationService = registrationService;
    }

    @GetMapping("/role")
    List<String> role() {
        return registrationService.role();
    }

    @PostMapping("/registration")
    Mono<ResponseEntity<RegistrationResponseDto>> createNewUser(@RequestBody RegistrationDto dto) {
        return registrationService.createNewUser(dto).map(course -> ResponseEntity.created(URI.create("/course")).body(course));
    }
}

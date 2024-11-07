package org.example.reactiveuniversity;

import org.example.reactiveuniversity.dto.RegistrationDto;
import org.example.reactiveuniversity.dto.RegistrationResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/user")

public class RegistrationController {
    private final RegistrationService registrationService;

    public RegistrationController(RegistrationService registrationService) {
        this.registrationService = registrationService;
    }

    @GetMapping("/role")
    @ResponseStatus(HttpStatus.OK)
    List<String> role() {
        return registrationService.role();
    }

    @PostMapping("/registration")
    @ResponseStatus(HttpStatus.CREATED)
    Mono<RegistrationResponseDto> createNewUser(@RequestBody RegistrationDto dto) {
        return registrationService.createNewUser(dto);
    }
}

package org.example.reactiveuniversity;

import org.example.reactiveuniversity.dto.RegistrationResponseDto;
import org.example.reactiveuniversity.dto.Teacher;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/registration")

public class RegistrationController {
    private final RegistrationService registrationService;

    public RegistrationController(RegistrationService registrationService) {
        this.registrationService = registrationService;
    }

    @GetMapping("/role")
    List<String> role() {

        return registrationService.role();
    }

    @PostMapping
    ResponseEntity<RegistrationResponseDto> createNewUser(@RequestBody RegistrationDto dto) {
        RegistrationResponseDto newUser = registrationService.createNewUser(dto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/registration").buildAndExpand(newUser).toUri();

        return ResponseEntity.created(uri).body(newUser);

    }
    @GetMapping("/{email}")
    Mono<Teacher>email(@PathVariable String email){
        return registrationService.email(email);
    }
}

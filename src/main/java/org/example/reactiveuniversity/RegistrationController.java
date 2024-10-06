package org.example.reactiveuniversity;

import org.example.reactiveuniversity.dto.RegistrationDto;
import org.example.reactiveuniversity.dto.RegistrationResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

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
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        RegistrationResponseDto newUser = registrationService.createNewUser(dto, authentication.getName());
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/registration").buildAndExpand(newUser).toUri();
        return ResponseEntity.created(uri).body(newUser);
    }
}

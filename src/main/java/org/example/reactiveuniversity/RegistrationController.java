package org.example.reactiveuniversity;

import org.example.reactiveuniversity.dto.RegistrationDto;
import org.example.reactiveuniversity.dto.RegistrationResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

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
    ResponseEntity<RegistrationResponseDto> createNewUser(@RequestBody RegistrationDto dto) {
        RegistrationResponseDto newUser = registrationService.createNewUser(dto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/registration").buildAndExpand(newUser).toUri();
        return ResponseEntity.created(uri).body(newUser);
    }
    @PostMapping("/change password")
        ResponseEntity<String>newPassword(@RequestParam String password){
            String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
            registrationService.changePassword(currentUsername,password);
            return ResponseEntity.ok("Ok");


        }

}

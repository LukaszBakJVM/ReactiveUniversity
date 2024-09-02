package org.example.teacher;

import org.example.teacher.dto.NewTeacherDto;
import org.example.teacher.dto.ResponseNewTeacherDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.net.URI;

@RestController
@RequestMapping("/teacher")
public class TeacherController {
    private final TeacherServices teacherServices;
    private final TeacherRepository repository;

    public TeacherController(TeacherServices teacherServices, TeacherRepository repository) {
        this.teacherServices = teacherServices;
        this.repository = repository;
    }

    @PostMapping
    Mono<ResponseEntity<ResponseNewTeacherDto>> addNewTeacher(@RequestBody NewTeacherDto dto) {
        return teacherServices.createTeacher(dto).map(teacher -> ResponseEntity.created(URI.create("/teacher")).body(teacher));

    }
    @GetMapping("/{email}")
    Mono<Teacher>yy(@PathVariable String email){
        return repository.findByEmail(email);
    }
}

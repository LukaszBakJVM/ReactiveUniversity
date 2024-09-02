package org.example.teacher;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.net.URI;

@RestController
@RequestMapping("/teacher")
public class TeacherController {
    private final TeacherServices teacherServices;

    public TeacherController(TeacherServices teacherServices) {
        this.teacherServices = teacherServices;
    }

    @PostMapping
    Mono<ResponseEntity<NewTeacherDto>> addNewTeacher(@RequestBody NewTeacherDto dto) {
        return teacherServices.newTeacher(dto).map(teacher -> ResponseEntity.created(URI.create("/teacher")).body(teacher));

    }
}

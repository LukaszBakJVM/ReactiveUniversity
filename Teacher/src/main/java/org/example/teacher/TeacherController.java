package org.example.teacher;

import org.example.teacher.dto.AddSchoolSubjects;
import org.example.teacher.dto.NewTeacherDto;
import org.example.teacher.dto.ResponseNewTeacherDto;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.List;

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

    @PostMapping("/update")
    Mono<ResponseEntity<AddSchoolSubjects>> updateSubject(@RequestBody List<String> subjects) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String name = authentication.getName();

        return teacherServices.addSchoolSubjectsMono(subjects, name).map(update -> ResponseEntity.created(URI.create("/teacher/update")).body(update));
    }

    @GetMapping("/{email}")
    Mono<Teacher> findTeacherByEmail(@PathVariable String email) {
        return repository.findByEmail(email);
    }
}

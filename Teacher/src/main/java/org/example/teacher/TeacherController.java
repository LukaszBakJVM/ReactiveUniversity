package org.example.teacher;

import org.example.teacher.dto.AddSchoolSubjects;
import org.example.teacher.dto.TeacherInfo;
import org.example.teacher.dto.WriteNewTeacherDto;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/teacher")
public class TeacherController {
    private final TeacherServices teacherServices;


    public TeacherController(TeacherServices teacherServices) {
        this.teacherServices = teacherServices;

    }

    @PostMapping
    Mono<ResponseEntity<WriteNewTeacherDto>> addNewTeacher(@RequestBody WriteNewTeacherDto dto) {
        return teacherServices.createTeacher(dto).map(teacher -> ResponseEntity.created(URI.create("/teacher")).body(teacher));

    }

    @PostMapping("/update")
    Mono<ResponseEntity<AddSchoolSubjects>> updateSubject(@RequestBody List<String> subjects) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String name = authentication.getName();

        return teacherServices.addSchoolSubjectsMono(subjects, name).map(update -> ResponseEntity.created(URI.create("/teacher/update")).body(update));
    }

    @GetMapping("/{email}")
    ResponseEntity<Mono<TeacherInfo>> teacherInfoByEmail(@PathVariable String email) {
        return ResponseEntity.ok(teacherServices.findTeacher(email));
    }

    @GetMapping("/all")
    ResponseEntity<Flux<TeacherInfo>> allTeacherInfo() {
        return ResponseEntity.ok(teacherServices.allTeacherInfo());
    }

}

package org.example.teacher;

import org.example.teacher.dto.AddSchoolSubjects;
import org.example.teacher.dto.TeacherPrivateInfo;
import org.example.teacher.dto.TeacherPublicInfo;
import org.example.teacher.dto.WriteNewTeacherDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
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
    Mono<ResponseEntity<WriteNewTeacherDto>> addNewTeacher(@RequestBody WriteNewTeacherDto dto) {
        return teacherServices.createTeacher(dto).map(teacher -> ResponseEntity.created(URI.create("/teacher")).body(teacher));

    }

    @PostMapping("/update")
    Mono<ResponseEntity<AddSchoolSubjects>> updateSubject(@RequestBody AddSchoolSubjects subjects) {
        return teacherServices.addSchoolSubjects(subjects).map(update -> ResponseEntity.created(URI.create("/teacher/update")).body(update));
    }

    @GetMapping("/private/{email}")
    ResponseEntity<Mono<TeacherPrivateInfo>> teacherInfoByEmail(@PathVariable String email) {
        return ResponseEntity.ok(teacherServices.findTeacher(email));
    }

    @GetMapping("/private/all")
    ResponseEntity<Flux<TeacherPrivateInfo>> allTeacherInfo() {
        return ResponseEntity.ok(teacherServices.allTeacherInfo());
    }

    @GetMapping("/info/{subject}")
    Flux<TeacherPublicInfo> teacherPublicInfoBySubject(@PathVariable String subject) {
        return teacherServices.teacherPublicInfo(subject);
    }

}

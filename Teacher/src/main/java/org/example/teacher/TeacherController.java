package org.example.teacher;

import org.example.teacher.dto.AddSchoolSubjects;
import org.example.teacher.dto.FindAllTeacherStudents;
import org.example.teacher.dto.TeacherPrivateInfo;
import org.example.teacher.dto.TeacherPublicInfo;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/teacher")
public class TeacherController {
    private final TeacherServices teacherServices;


    public TeacherController(TeacherServices teacherServices) {
        this.teacherServices = teacherServices;

    }


    @PutMapping("/update")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    Mono<AddSchoolSubjects> updateSubject(@RequestBody AddSchoolSubjects subjects) {
        return teacherServices.addSchoolSubjects(subjects);
    }

    @GetMapping("/private/{email}")
    @ResponseStatus(HttpStatus.OK)
    ResponseEntity<Mono<TeacherPrivateInfo>> teacherInfoByEmail(@PathVariable String email) {
        return ResponseEntity.ok(teacherServices.findTeacher(email));
    }

    @GetMapping("/private/all")
    @ResponseStatus(HttpStatus.OK)
    ResponseEntity<Flux<TeacherPrivateInfo>> allTeacherInfo() {
        return ResponseEntity.ok(teacherServices.allTeacherInfo());
    }

    @GetMapping("/info/{subject}")
    Mono<TeacherPublicInfo> teacherPublicInfoBySubject(@PathVariable String subject) {
        return teacherServices.teacherPublicInfo(subject);
    }

    @GetMapping("/my-students")
    @ResponseStatus(HttpStatus.OK)
    Flux<Void> findAllTeacherStudents() {
        return teacherServices.findAllMyStudents();
    }


}

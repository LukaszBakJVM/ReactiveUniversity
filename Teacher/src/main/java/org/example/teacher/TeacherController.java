package org.example.teacher;

import org.example.teacher.dto.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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

    @PreAuthorize("hasRole('Office')")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    Mono<WriteNewTeacherDto> addNewTeacher(@RequestBody WriteNewTeacherDto dto) {
        return teacherServices.createTeacher(dto);

    }


    @PreAuthorize("hasRole('Office')")
    @PutMapping("/update")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    Mono<AddSchoolSubjects> updateSubject(@RequestBody AddSchoolSubjects subjects) {
        return teacherServices.addSchoolSubjects(subjects);
    }

    @PreAuthorize("hasAnyRole('Office','Teacher')")
    @GetMapping("/private/{email}")
    @ResponseStatus(HttpStatus.OK)
    ResponseEntity<Mono<TeacherPrivateInfo>> teacherInfoByEmail(@PathVariable String email) {
        return ResponseEntity.ok(teacherServices.findTeacher(email));
    }

    @PreAuthorize("hasAnyRole('Office','Teacher')")
    @GetMapping("/private/all")
    @ResponseStatus(HttpStatus.OK)
    ResponseEntity<Flux<TeacherPrivateInfo>> allTeacherInfo() {
        return ResponseEntity.ok(teacherServices.allTeacherInfo());
    }

    @GetMapping("/info/{subject}")
    Mono<TeacherPublicInfo> teacherPublicInfoBySubject(@PathVariable String subject) {
        return teacherServices.teacherPublicInfo(subject);
    }

    @PreAuthorize("hasRole('Teacher')")
    @GetMapping("/my-students")
    @ResponseStatus(HttpStatus.OK)
    Flux<FindAllTeacherStudents> findAllTeacherStudents() {
        return teacherServices.findAllMyStudents();
    }


}

package org.example.student;

import org.example.student.dto.AddCourse;
import org.example.student.dto.StudentInfo;
import org.example.student.dto.WriteNewPerson;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;

@RestController
@RequestMapping("/student")
public class StudentController {
    private final StudentServices studentServices;

    public StudentController(StudentServices studentServices) {
        this.studentServices = studentServices;
    }

    @PostMapping
    Mono<ResponseEntity<WriteNewPerson>> addStudent(@RequestBody WriteNewPerson student) {
        return studentServices.createStudent(student).map(person -> ResponseEntity.created(URI.create("/student")).body(person));
    }

    @PostMapping("/update")
    Mono<ResponseEntity<AddCourse>> addCourseToStudent(@RequestBody AddCourse writeCourse) {
        return studentServices.addCourseToStudent(writeCourse).map(course -> ResponseEntity.created(URI.create("/student/update")).body(course));

    }

    @GetMapping("/{email}")
    Flux<StudentInfo> findStudentsByEmail(@PathVariable String email) {
        return studentServices.findStudentsByEmail(email);
    }
    @GetMapping("/assigned")
    Flux<StudentInfo>allStudentWithCourse(){
        return studentServices.allStudentsWithCourse();

    }
  //  @GetMapping("/unassigned")
  //  Flux<StudentInfo>

}

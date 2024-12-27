package org.example.student;

import org.example.student.dto.AddCourse;
import org.example.student.dto.StudentInfoWithCourse;
import org.example.student.dto.StudentInfoWithoutCourse;
import org.example.student.dto.Teacher;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/student")
public class StudentController {
    private final StudentServices studentServices;


    public StudentController(StudentServices studentServices) {
        this.studentServices = studentServices;
    }


    @PutMapping("/update")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    Mono<AddCourse> addCourseToStudent(@RequestBody AddCourse writeCourse) {
        return studentServices.addCourseToStudent(writeCourse);

    }

    @GetMapping("/{email}")
    @ResponseStatus(HttpStatus.OK)
    Flux<StudentInfoWithCourse> findStudentsByEmail(@PathVariable String email) {
        return studentServices.findStudentsByEmail(email);
    }

    @GetMapping("/assigned")
    @ResponseStatus(HttpStatus.OK)
    Flux<StudentInfoWithCourse> allStudentWithCourse() {
        return studentServices.allStudentsWithCourse();

    }

    @GetMapping("/unassigned")
    @ResponseStatus(HttpStatus.OK)
    Flux<StudentInfoWithoutCourse> allStudentWithoutCurses() {
        return studentServices.allStudentsWithoutCourse();
    }


    @GetMapping("/studentInfo/{course}")
    @ResponseStatus(HttpStatus.OK)
    Flux<StudentInfoWithCourse> infoWithCourse(@PathVariable String course) {
        return studentServices.studentInfoByCourse(course);

    }

    @GetMapping("/teachers")
    Mono<List<Teacher>> getMyTeachers() {
        return studentServices.findMyTeachers();
    }


}

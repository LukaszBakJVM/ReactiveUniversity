package org.example.student.grades;

import org.example.student.grades.dto.GradesRequest;
import org.example.student.grades.dto.GradesResponse;
import org.example.student.grades.dto.StudentGrades;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/grades")
public class GradesController {
    private final GradesServices gradesServices;


    public GradesController(GradesServices gradesServices) {
        this.gradesServices = gradesServices;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    Mono<GradesResponse> setGrates(@RequestBody GradesRequest request) {
        return gradesServices.grade(request);
    }

    @GetMapping("/my-grades")
    @ResponseStatus(HttpStatus.OK)
    Flux<StudentGrades> myGrades() {
        return gradesServices.findOwnGrades();
    }

    @GetMapping("/{email}")
    @ResponseStatus(HttpStatus.OK)
    Mono<StudentGrades> teacherGrades(@PathVariable String email, @RequestParam String subject) {
        return gradesServices.gradesForTeacher(email, subject);
    }

}

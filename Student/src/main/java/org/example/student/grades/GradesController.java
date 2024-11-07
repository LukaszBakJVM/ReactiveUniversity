package org.example.student.grades;

import org.example.student.grades.dto.GradesRequest;
import org.example.student.grades.dto.GradesResponse;
import org.example.student.grades.dto.StudentGrades;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;

@RestController
@RequestMapping("/grades")
public class GradesController {
    private final GradesServices gradesServices;


    public GradesController(GradesServices gradesServices) {
        this.gradesServices = gradesServices;
    }

    @PostMapping
    Mono<ResponseEntity<GradesResponse>> setGrates(@RequestBody GradesRequest request) {
        return gradesServices.grade(request).map(grades -> ResponseEntity.created(URI.create("/grades")).body(grades));
    }
    @GetMapping("/my-grades")
    Flux<StudentGrades>myGrades(){
        return gradesServices.findOwnGrades();
    }

}

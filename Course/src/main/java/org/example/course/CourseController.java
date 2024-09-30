package org.example.course;

import org.example.course.dto.CourseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;

@RestController
@RequestMapping("/course")
public class CourseController {
    private final CourseServices courseServices;

    public CourseController(CourseServices courseServices) {
        this.courseServices = courseServices;
    }

    @PostMapping
    Mono<ResponseEntity<CourseDto>> createCourse(@RequestBody CourseDto dto) {
        return courseServices.createCourse(dto).map(course -> ResponseEntity.created(URI.create("/course")).body(course));
    }

    @DeleteMapping("/{courseName}")
    public Mono<ResponseEntity<Void>> deleteCourse(@PathVariable String courseName) {
        return courseServices.deleteCourse(courseName).then(Mono.just(ResponseEntity.noContent().build()));

    }

    @GetMapping("/all")
    ResponseEntity<Flux<CourseDto>> finaAllCourses() {
        return ResponseEntity.ok(courseServices.findAll());
    }
    @GetMapping("/{subject}/name")
    ResponseEntity<Flux<CourseDto>>courseInfoBySubjectName(@PathVariable String subject){
        return ResponseEntity.ok(courseServices.findCourseBySubject(subject));
    }


}

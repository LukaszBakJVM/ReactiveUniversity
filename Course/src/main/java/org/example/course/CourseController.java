package org.example.course;

import org.example.course.dto.CourseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/course")
public class CourseController {
    private final CourseServices courseServices;

    public CourseController(CourseServices courseServices) {
        this.courseServices = courseServices;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    Mono<CourseDto> createCourse(@RequestBody CourseDto dto) {
        return courseServices.createOrUpdateCourse(dto);
    }

    @DeleteMapping("/{courseName}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> deleteCourse(@PathVariable String courseName) {
        return courseServices.deleteCourse(courseName);

    }

    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    ResponseEntity<Flux<CourseDto>> allCourseInfo() {
        return ResponseEntity.ok(courseServices.findAll());
    }

    @GetMapping("/{subject}/name")
    @ResponseStatus(HttpStatus.OK)
    ResponseEntity<Flux<CourseDto>> courseInfoBySubjectName(@PathVariable String subject) {
        return ResponseEntity.ok(courseServices.findCourseBySubject(subject));
    }

    @GetMapping("/{course}")
    @ResponseStatus(HttpStatus.OK)
    ResponseEntity<Mono<CourseDto>> courseInfo(@PathVariable String course) {
        return ResponseEntity.ok(courseServices.courseInfo(course));
    }

}

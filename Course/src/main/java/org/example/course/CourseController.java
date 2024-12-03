package org.example.course;

import org.example.course.dto.CourseDto;
import org.springframework.http.HttpStatus;
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
    Flux<CourseDto> allCourseInfo() {
        return courseServices.findAll();
    }

    @GetMapping("/{subject}/name")
    @ResponseStatus(HttpStatus.OK)
    Flux<CourseDto> courseInfoBySubjectName(@PathVariable String subject) {
        return courseServices.findCourseBySubject(subject);
    }

    @GetMapping("/{course}")
    @ResponseStatus(HttpStatus.OK)
    Mono<CourseDto> courseInfo(@PathVariable String course) {
        return courseServices.courseInfo(course);
    }

}

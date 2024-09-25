package org.example.course;

import org.example.course.dto.CourseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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


}

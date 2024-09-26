package org.example.course;

import org.example.course.dto.CourseDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.HashSet;

@Service
public class CourseServices {
    private final WebClient.Builder webClient;
    private final CourseRepository courseRepository;
    private final CourseMapper courseMapper;
    @Value("${subject}")
    private String subjectUrl;
    @Value("${student}")
    private String studentUrl;

    public CourseServices(WebClient.Builder webClient, CourseRepository courseRepository, CourseMapper courseMapper) {
        this.webClient = webClient;
        this.courseRepository = courseRepository;
        this.courseMapper = courseMapper;
    }

    Mono<CourseDto> createCourse(CourseDto dto) {
        return courseRepository.findByCourseName(dto.courseName()).flatMap(course -> {
            course.setId(course.getId());
            course.getSubjectName().addAll(new HashSet<>(dto.subject()));
            return courseRepository.save(course);
        }).switchIfEmpty(courseRepository.save(courseMapper.dtoToEntity(dto))).map(courseMapper::entityToDto);
    }

    Mono<Void> deleteCourse(String courseName) {
      return   courseRepository.deleteByCourseName(courseName);
    }

    Flux<CourseDto> findAll() {
        return courseRepository.findAll().map(courseMapper::entityToDto);
    }
}


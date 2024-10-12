package org.example.course;

import org.example.course.dto.CourseDto;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.HashSet;

@Service
public class CourseServices {

    private final CourseRepository courseRepository;
    private final CourseMapper courseMapper;


    public CourseServices(CourseRepository courseRepository, CourseMapper courseMapper) {

        this.courseRepository = courseRepository;
        this.courseMapper = courseMapper;
    }

    Mono<CourseDto> createOrUpdateCourse(CourseDto dto) {
        return courseRepository.findByCourseName(dto.courseName()).flatMap(course -> {
            course.setId(course.getId());
            course.getSubjectName().addAll(new HashSet<>(dto.subjects()));
            return courseRepository.save(course);
        }).switchIfEmpty(courseRepository.save(courseMapper.dtoToEntity(dto))).map(courseMapper::entityToDto);
    }

    Mono<Void> deleteCourse(String courseName) {
        return courseRepository.deleteByCourseName(courseName);
    }

    Flux<CourseDto> findAll() {
        return courseRepository.findAll().map(courseMapper::entityToDto);
    }

    Flux<CourseDto> findCourseBySubject(String subjectName) {
        return courseRepository.findCourseBySubjectNameContaining(subjectName).map(courseMapper::entityToDto);
    }

    Mono<CourseDto> courseInfo(String courseName) {
        return courseRepository.findByCourseName(courseName).map(courseMapper::entityToDto);
    }
}


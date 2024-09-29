package org.example.course;

import org.example.course.dto.CourseDto;
import org.example.course.dto.Subject;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CourseMapper {
    Course dtoToEntity(CourseDto dto) {

        Course course = new Course();
        course.setCourseName(dto.courseName());
        course.setSubjectName(dto.subject());
        return course;
    }

    CourseDto entityToDto(Course course) {

        return new CourseDto(course.getCourseName(), course.getSubjectName());
    }

    private Subject mapToSubject(String subject) {
        return new Subject(subject);
    }

    private Set<String> subjectsStrings(Set<Subject> subjects) {
        return subjects.stream().map(Subject::subject).collect(Collectors.toSet());
    }
}

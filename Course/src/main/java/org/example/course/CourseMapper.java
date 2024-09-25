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
        Set<String> stringSet = subjectsStrings(dto.subject());

        course.setSubjectName(stringSet);
        return course;
    }

    CourseDto entityToDto(Course course) {
        Set<Subject> collect = course.getSubjectName().stream().map(this::mapToSubject).collect(Collectors.toSet());
        return new CourseDto(course.getCourseName(), collect);
    }

    private Subject mapToSubject(String subject) {
        return new Subject(subject);
    }

    private Set<String> subjectsStrings(Set<Subject> subjects) {
        return subjects.stream().map(Subject::subject).collect(Collectors.toSet());
    }
}

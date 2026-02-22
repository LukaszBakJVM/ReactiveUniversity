package org.example.course;

import org.example.course.dto.CourseDto;
import org.example.course.dto.CoursesList;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourseMapper {
    Course dtoToEntity(CourseDto dto) {

        Course course = new Course();
        course.setCourseName(dto.courseName());
        course.setSubjectName(dto.subjects());
        return course;
    }

    CourseDto entityToDto(Course course) {
        return new CourseDto(course.getCourseName(), course.getSubjectName());
    }

    CoursesList toList(List<Course> courses) {
        return new CoursesList(courses.stream().map(this::entityToDto).toList());


    }


}

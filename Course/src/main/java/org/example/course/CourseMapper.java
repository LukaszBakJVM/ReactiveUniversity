package org.example.course;

import org.example.course.dto.CourseDto;
import org.springframework.stereotype.Service;

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


}

package org.example.course.dto;

import java.util.Set;

public record CourseDto(String courseName,Set<String> subjects) {
}

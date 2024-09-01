package org.example.teacher;

import java.util.List;

public record TeacherDto(String firstName, String lastName, int age, String email, List<String> subjectName) {
}

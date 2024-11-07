package org.example.student.grades.dto;

import java.util.List;

public record StudentGrades(String subject, List<String> grades, String teacher) {
}

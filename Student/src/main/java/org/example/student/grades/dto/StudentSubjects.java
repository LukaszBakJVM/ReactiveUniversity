package org.example.student.grades.dto;

import java.util.List;

public record StudentSubjects(List<String>subjects,StudentGrades grades) {
}

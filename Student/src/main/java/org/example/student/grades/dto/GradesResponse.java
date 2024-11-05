package org.example.student.grades.dto;

import java.util.List;

public record GradesResponse(String email,String subject, List<String> gradesDescription) {
}

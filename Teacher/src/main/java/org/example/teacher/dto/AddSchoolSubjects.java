package org.example.teacher.dto;

import java.util.Set;

public record AddSchoolSubjects(String email,Set<String> subjects) {
}

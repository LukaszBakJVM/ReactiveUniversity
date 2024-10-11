package org.example.teacher.dto;

import java.util.Set;

public record TeacherPrivateInfo(String firstName, String lastName, String email, Set<String> subjectName) {
}

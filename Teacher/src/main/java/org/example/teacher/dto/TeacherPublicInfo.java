package org.example.teacher.dto;

import java.util.Set;

public record TeacherPublicInfo(String firstName, String lastName, Set<String> subjectName) {
}

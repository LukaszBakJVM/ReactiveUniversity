package org.example.teacher.dto;

import java.util.List;

public record FindAllTeacherStudents(String firstName, String lastName,String email, String course, String subject,
                                     List<String> grades) {
}

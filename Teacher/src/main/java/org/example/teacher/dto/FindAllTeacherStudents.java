package org.example.teacher.dto;

import java.util.List;

public record FindAllTeacherStudents(Student student,List<Grades>grades) {
}

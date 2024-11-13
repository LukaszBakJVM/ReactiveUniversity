package org.example.student.grades;

import org.example.student.grades.dto.GradesRequest;
import org.example.student.grades.dto.GradesResponse;
import org.example.student.grades.dto.StudentGrades;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class GradesMapper {
    Grades dtoToEntity(GradesRequest dto, String teacher, LocalDate date) {
        Grades grades = new Grades();
        grades.setEmail(dto.email());
        grades.setSubject(dto.subject());
        grades.getGradesDescription().add(dto.gradesDescription() + "  " + date);
        grades.setTeacher(teacher);
        return grades;
    }

    GradesResponse entityToDto(Grades grades) {
        return new GradesResponse(grades.getEmail(), grades.getSubject(), grades.getGradesDescription().getLast());
    }

    StudentGrades studentGrades(Grades grades) {
        return new StudentGrades(grades.getSubject(), grades.getGradesDescription(), grades.getTeacher());
    }
    StudentGrades gradesForTeacher(Grades grades){
        return new StudentGrades(grades.getSubject(),grades.getGradesDescription(),grades.getTeacher());
    }

}


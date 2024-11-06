package org.example.student.grades;

import org.example.student.grades.dto.GradesRequest;
import org.example.student.grades.dto.GradesResponse;
import org.springframework.stereotype.Service;

@Service
public class GradesMapper {
    Grades dtoToEntity(GradesRequest dto, String teacher) {
        Grades grades = new Grades();
        grades.setEmail(dto.email());
        grades.setSubject(dto.subject());
        grades.getGradesDescription().add(dto.gradesDescription());
        grades.setTeacher(teacher);
        return grades;
    }

    GradesResponse entityToDto(Grades grades) {
        return new GradesResponse(grades.getEmail(), grades.getSubject(), grades.getGradesDescription());
    }

}


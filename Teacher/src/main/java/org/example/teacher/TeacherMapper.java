package org.example.teacher;

import org.example.teacher.dto.AddSchoolSubjects;
import org.example.teacher.dto.NewTeacherDto;
import org.example.teacher.dto.ResponseNewTeacherDto;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class TeacherMapper {
    Teacher dtoToEntity(NewTeacherDto dto) {
        Teacher teacher = new Teacher();
        teacher.setFirstName(dto.firstName());
        teacher.setLastName(dto.lastName());
        teacher.setAge(dto.age());
        teacher.setEmail(dto.email());
        teacher.setPassword(dto.password());
        teacher.setSubjectName(new ArrayList<>());
        return teacher;


    }

    ResponseNewTeacherDto entityToDto(Teacher teacher) {
        return new ResponseNewTeacherDto(teacher.getFirstName(), teacher.getLastName(), teacher.getAge(), teacher.getEmail());
    }

    AddSchoolSubjects addSchoolSubjects(Teacher teacher) {
        return new AddSchoolSubjects(teacher.getSubjectName());
    }
}

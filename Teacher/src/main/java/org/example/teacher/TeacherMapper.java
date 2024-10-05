package org.example.teacher;

import org.example.teacher.dto.AddSchoolSubjects;
import org.example.teacher.dto.TeacherInfo;
import org.example.teacher.dto.WriteNewTeacherDto;
import org.springframework.stereotype.Service;

import java.util.HashSet;

@Service
public class TeacherMapper {


    Teacher dtoToEntity(WriteNewTeacherDto dto) {
        Teacher teacher = new Teacher();
        teacher.setFirstName(dto.firstName());
        teacher.setLastName(dto.lastName());
        teacher.setEmail(dto.email());
        teacher.setSubjectName(new HashSet<>());

        return teacher;


    }

    WriteNewTeacherDto entityToDto(Teacher teacher) {
        return new WriteNewTeacherDto(teacher.getFirstName(), teacher.getLastName(), teacher.getEmail());
    }

    AddSchoolSubjects addSchoolSubjects(Teacher teacher) {
        return new AddSchoolSubjects(teacher.getSubjectName());
    }

    TeacherInfo teacherInfo(Teacher teacher) {
        return new TeacherInfo(teacher.getFirstName(), teacher.getLastName(), teacher.getEmail(), teacher.getSubjectName());
    }


}

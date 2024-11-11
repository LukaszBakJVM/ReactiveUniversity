package org.example.teacher;

import org.example.teacher.dto.*;
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

    AddSchoolSubjects addSchoolSubjectsToDto(Teacher teacher) {
        return new AddSchoolSubjects(teacher.getEmail(),teacher.getSubjectName());
    }


    TeacherPrivateInfo teacherPrivateInfo(Teacher teacher) {
        return new TeacherPrivateInfo(teacher.getFirstName(), teacher.getLastName(), teacher.getEmail(), teacher.getSubjectName());
    }

    TeacherPublicInfo teacherPublicInfo(Teacher teacher) {
        return new TeacherPublicInfo(teacher.getFirstName(), teacher.getLastName(), teacher.getSubjectName());
    }
    TeacherSubjects email(Teacher teacher){
        return new TeacherSubjects(teacher.getSubjectName());
    }


}

package org.example.teacher;

import org.springframework.stereotype.Service;

@Service
public class TeacherMapper {
    Teacher dtoToEntity(TeacherDto dto){
        return new Teacher(dto.firstName(), dto.lastName(), dto.age(), dto.email(), dto.subjectName());
    }
    TeacherDto entityToDto(Teacher teacher){
        return new TeacherDto(teacher.firstName(),teacher.lastName(),teacher.age(),teacher.email(),teacher.subjectName());
    }
}

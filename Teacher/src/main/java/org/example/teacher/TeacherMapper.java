package org.example.teacher;

import org.springframework.stereotype.Service;

@Service
public class TeacherMapper {
    Teacher dtoToEntity(NewTeacherDto dto){
        return new Teacher(dto.firstName(), dto.lastName(), dto.age(), dto.email());
    }
    NewTeacherDto entityToDto(Teacher teacher){
        return new NewTeacherDto(teacher.firstName(),teacher.lastName(),teacher.age(),teacher.email());
    }
}

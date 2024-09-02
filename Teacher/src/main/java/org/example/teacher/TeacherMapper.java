package org.example.teacher;

import org.example.teacher.dto.NewTeacherDto;
import org.example.teacher.dto.ResponseNewTeacherDto;
import org.springframework.stereotype.Service;

@Service
public class TeacherMapper {
    Teacher dtoToEntity(NewTeacherDto dto){
        return new Teacher(dto.firstName(), dto.lastName(), dto.age(), dto.email(), dto.password());
    }
    ResponseNewTeacherDto entityToDto(Teacher teacher){
        return new ResponseNewTeacherDto(teacher.firstName(),teacher.lastName(),teacher.age(),teacher.email());
    }
}

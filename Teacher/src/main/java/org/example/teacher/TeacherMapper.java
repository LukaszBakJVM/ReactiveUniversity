package org.example.teacher;

import org.example.teacher.dto.AddSchoolSubjects;
import org.example.teacher.dto.NewTeacherDto;
import org.example.teacher.dto.ResponseNewTeacherDto;
import org.example.teacher.dto.TeacherLogin;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;

@Service
public class TeacherMapper {
    private final String ROLE = "Teacher";
    private final PasswordEncoder passwordEncoder;

    public TeacherMapper(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    Teacher dtoToEntity(NewTeacherDto dto) {
        Teacher teacher = new Teacher();
        teacher.setFirstName(dto.firstName());
        teacher.setLastName(dto.lastName());
        teacher.setAge(dto.age());
        teacher.setEmail(dto.email());
        String password = passwordEncoder.encode(dto.password());
        teacher.setPassword(password);
        teacher.setSubjectName(new HashSet<>());
        teacher.setRole(ROLE);
        return teacher;


    }

    ResponseNewTeacherDto entityToDto(Teacher teacher) {
        return new ResponseNewTeacherDto(teacher.getFirstName(), teacher.getLastName(), teacher.getAge(), teacher.getEmail());
    }

    AddSchoolSubjects addSchoolSubjects(Teacher teacher) {
        return new AddSchoolSubjects(teacher.getSubjectName());
    }

    TeacherLogin login(Teacher teacher) {
        return new TeacherLogin(teacher.getEmail(), teacher.getPassword(), teacher.getRole());
    }
}

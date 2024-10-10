package org.example.student;

import org.example.student.dto.WriteNewPerson;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class StudentServices {


    private final StudentMapper studentMapper;
    private final StudentRepository studentRepository;


    public StudentServices(StudentMapper studentMapper, StudentRepository studentRepository) {
        this.studentMapper = studentMapper;
        this.studentRepository = studentRepository;

    }

    Mono<WriteNewPerson> createStudent(WriteNewPerson person) {
        return studentRepository.save(studentMapper.dtoToStudent(person)).map(studentMapper::studentToDto);
    }
}

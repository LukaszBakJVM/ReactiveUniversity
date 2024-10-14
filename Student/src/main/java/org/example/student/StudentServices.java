package org.example.student;

import org.example.student.dto.AddCourse;
import org.example.student.dto.StudentInfo;
import org.example.student.dto.WriteNewPerson;
import org.example.student.exception.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;

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

    Mono<AddCourse> addCourseToStudent(AddCourse addCourse) {
        return studentRepository.findByEmail(addCourse.studentEmail()).flatMap(student -> {
            student.setId(student.getId());
            student.setCourse(addCourse.course());
            return studentRepository.save(student);
        }).map(studentMapper::addCourseResponse);
    }
 //   Mono<List<StudentInfo>>findStudentsByEmail(String email){
      //  Mono<List<Student>> byEmailContaining = studentRepository.findByEmailContaining(email).map(m->studentMapper.info(m));
      //  r


    }

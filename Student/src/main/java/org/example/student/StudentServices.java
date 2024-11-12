package org.example.student;

import org.example.student.dto.*;
import org.example.student.exception.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
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

    Mono<AddCourse> addCourseToStudent(AddCourse addCourse) {
        return studentRepository.findByEmail(addCourse.studentEmail()).flatMap(student -> {
            student.setId(student.getId());
            student.setCourse(addCourse.course());
            return studentRepository.save(student);
        }).map(studentMapper::addCourseResponse).switchIfEmpty(Mono.error(new UsernameNotFoundException(String.format("Student %s not found",addCourse.studentEmail()))));
    }

    Flux<StudentInfoWithCourse> findStudentsByEmail(String email) {
      return studentRepository.findByEmailContaining(email).map(studentMapper::studentInfoWithCourse);
    }
    Flux<StudentInfoWithCourse>allStudentsWithCourse(){
        return studentRepository.findAll().filter(c-> c.getCourse() != null).map(studentMapper::studentInfoWithCourse);
    }
    Flux<StudentInfoWithoutCourse>allStudentsWithoutCourse(){
        return studentRepository.findAll().filter(c-> c.getCourse() == null).map(studentMapper::studentInfoWithoutCourse);
    }
    Flux<StudentEmail>findStudentsByEmails(String emailContaining){
        return studentRepository.findByEmailContaining(emailContaining).map(studentMapper::email);
    }
    Flux<StudentInfoWithCourse>studentInfoByCourse(String course){
        return studentRepository.findByCourse(course).map(studentMapper::studentInfoWithCourse);
    }

}

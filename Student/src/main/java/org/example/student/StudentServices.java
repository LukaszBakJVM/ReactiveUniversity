package org.example.student;

import org.example.student.dto.*;
import org.example.student.exception.ConnectionException;
import org.example.student.exception.UsernameNotFoundException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.security.Principal;
import java.util.List;

@Service
public class StudentServices {


    private final StudentMapper studentMapper;
    private final StudentRepository studentRepository;
    private final WebClient.Builder webClient;
    @Value("${courseUrl}")
    private String courseUrl;
    @Value("${teacherUrl}")
    private String teacherUrl;


    public StudentServices(StudentMapper studentMapper, StudentRepository studentRepository, WebClient.Builder webClient) {
        this.studentMapper = studentMapper;
        this.studentRepository = studentRepository;
        this.webClient = webClient;
    }

    Mono<WriteNewPerson> createStudent(WriteNewPerson person) {
        return studentRepository.save(studentMapper.dtoToStudent(person)).map(studentMapper::studentToDto);
    }

    Mono<AddCourse> addCourseToStudent(AddCourse addCourse) {
        return studentRepository.findByEmail(addCourse.studentEmail()).flatMap(student -> {
            student.setId(student.getId());
            student.setCourse(addCourse.course());
            return studentRepository.save(student);
        }).map(studentMapper::addCourseResponse).switchIfEmpty(Mono.error(new UsernameNotFoundException(String.format("Student %s not found", addCourse.studentEmail()))));
    }

    Flux<StudentInfoWithCourse> findStudentsByEmail(String email) {
        return studentRepository.findByEmailContaining(email).map(studentMapper::studentInfoWithCourse);
    }

    Flux<StudentInfoWithCourse> allStudentsWithCourse() {
        return studentRepository.findAll().filter(c -> c.getCourse() != null).map(studentMapper::studentInfoWithCourse);
    }

    Flux<StudentInfoWithoutCourse> allStudentsWithoutCourse() {
        return studentRepository.findAll().filter(c -> c.getCourse() == null).map(studentMapper::studentInfoWithoutCourse);
    }

    Flux<StudentEmail> findStudentsByEmails(String emailContaining) {
        return studentRepository.findByEmailContaining(emailContaining).map(studentMapper::email);
    }

    Flux<StudentInfoWithCourse> studentInfoByCourse(String course) {
        return studentRepository.findByCourse(course).map(studentMapper::studentInfoWithCourse);
    }

    Mono<List<Teacher>> findMyTeachers() {
        return name().flatMap(email -> studentRepository.findByEmail(email).map(Student::getCourse).flatMap(this::findSubjectsByCourse).map(SubjectsByCourse::subjects).flatMapMany(Flux::fromIterable).flatMap(this::findTeachersBySubjects).distinct().collectList());


    }


    Mono<SubjectsByCourse> findSubjectsByCourse(String course) {
        return webClient.baseUrl(courseUrl).build().get().uri("/course/{course}", course).accept(MediaType.APPLICATION_JSON).retrieve().bodyToMono(SubjectsByCourse.class).onErrorResume(WebClientRequestException.class, response -> Mono.error(new ConnectionException("Connection refused : course ")));
    }

    Mono<Teacher> findTeachersBySubjects(String subject) {
        return webClient.baseUrl(teacherUrl).build().get().uri("/teacher/info/{subject}", subject).accept(MediaType.APPLICATION_JSON).retrieve().bodyToMono(Teacher.class).onErrorResume(WebClientRequestException.class, response -> Mono.error(new ConnectionException("Connection refused : teacher")));
    }


    private Mono<String> name() {
        return ReactiveSecurityContextHolder.getContext().map(SecurityContext::getAuthentication).map(Principal::getName);

    }
}

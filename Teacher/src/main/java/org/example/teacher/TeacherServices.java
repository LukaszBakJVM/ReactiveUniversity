package org.example.teacher;

import org.example.teacher.dto.*;
import org.example.teacher.exception.ConnectionException;
import org.example.teacher.exception.UsernameNotFoundException;
import org.example.teacher.exception.WrongCredentialsException;
import org.example.teacher.security.token.TokenStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
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
public class TeacherServices {

    private final TeacherMapper teacherMapper;
    private final TeacherRepository teacherRepository;
    private final WebClient.Builder webClient;
    private final TokenStore tokenStore;
    @Value("${student}")
    private String studentUrl;
    @Value("${course}")
    private String courseUrl;


    public TeacherServices(TeacherMapper teacherMapper, TeacherRepository teacherRepository, WebClient.Builder webClient, TokenStore tokenStore) {
        this.teacherMapper = teacherMapper;
        this.teacherRepository = teacherRepository;
        this.webClient = webClient;
        this.tokenStore = tokenStore;
    }

    Mono<WriteNewTeacherDto> createTeacher(WriteNewTeacherDto dto) {
        return teacherRepository.save(teacherMapper.dtoToEntity(dto)).map(teacherMapper::entityToDto);


    }

    Mono<AddSchoolSubjects> addSchoolSubjects(AddSchoolSubjects schoolSubjects) {
        return teacherRepository.findByEmail(schoolSubjects.email()).flatMap(teacher -> {
            teacher.setId(teacher.getId());
            teacher.getSubjectName().addAll(schoolSubjects.subjects());
            return teacherRepository.save(teacher);
        }).map(teacherMapper::addSchoolSubjectsToDto).switchIfEmpty(Mono.error(new UsernameNotFoundException(String.format("Teacher %s not found", schoolSubjects.email()))));

    }

    Mono<TeacherPrivateInfo> findTeacher(String email) {
        return teacherRepository.findByEmail(email).map(teacherMapper::teacherPrivateInfo);
    }

    Flux<TeacherPrivateInfo> allTeacherInfo() {
        return teacherRepository.findAll().map(teacherMapper::teacherPrivateInfo);
    }

    Mono<TeacherPublicInfo> teacherPublicInfo(String subjectName) {
        return teacherRepository.findTeacherBySubjectNameContains(subjectName).map(teacherMapper::teacherPublicInfo);
    }

    Flux<FindAllTeacherStudents> findAllMyStudents() {
        return name().flatMap(teacherRepository::findByEmail).map(teacherMapper::email).map(TeacherSubjects::subjects).flatMapMany(Flux::fromIterable).flatMap(this::findCourseBySubject).flatMap(this::finaAllUniqueStudents).distinct().flatMap(e -> allGrades(e.email()).map(grades -> new FindAllTeacherStudents(new Student(e.firstName(), e.lastName(), e.email(), e.course()), grades)));


    }

    private Flux<String> findCourseBySubject(String subject) {
        return webClient.baseUrl(courseUrl).build().get().uri("/course/{subject}/name", subject).accept(MediaType.APPLICATION_JSON).retrieve().bodyToFlux(CourseName.class).map(CourseName::courseName).onErrorResume(WebClientRequestException.class, response -> Mono.error(new ConnectionException("Connection Error")));
    }

    private Flux<Student> finaAllUniqueStudents(String course) {
        return webClient.baseUrl(studentUrl).build().get().uri("student/studentInfo/{course}", course).accept(MediaType.APPLICATION_JSON).retrieve().bodyToFlux(Student.class).onErrorResume(WebClientRequestException.class, response -> Mono.error(new ConnectionException("Connection Error")));
    }

    Mono<List<Grades>> allGrades(String email) {
        String authorization = "Authorization";
        return name().flatMap(n -> webClient.baseUrl(studentUrl).build().get().uri("/grades/{email}", email).header(authorization, "Bearer %s".formatted(tokenStore.getToken(n))).accept(MediaType.APPLICATION_JSON).retrieve().onStatus(HttpStatusCode::is4xxClientError, response -> Mono.error(new WrongCredentialsException("Wrong credentials"))).bodyToFlux(Grades.class).collectList()).onErrorResume(WebClientRequestException.class, response -> Mono.error(new ConnectionException("Connection Error")));
    }

    private Mono<String> name() {
        return ReactiveSecurityContextHolder.getContext().map(SecurityContext::getAuthentication).map(Principal::getName);


    }
}



package org.example.teacher;

import org.example.teacher.dto.*;
import org.example.teacher.exception.UsernameNotFoundException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.security.Principal;

@Service
public class TeacherServices {

    @Value("${student}")
    private String studentUrl;
    @Value("${subject}")
    private String subjectUrl;
    @Value("${course}")
    private String courseUrl;


    private final TeacherMapper teacherMapper;
    private final TeacherRepository teacherRepository;
    private final WebClient webClient;


    public TeacherServices(TeacherMapper teacherMapper, TeacherRepository teacherRepository, WebClient.Builder webClient) {
        this.teacherMapper = teacherMapper;
        this.teacherRepository = teacherRepository;
        this.webClient = webClient.build();
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

    Flux<TeacherPublicInfo> teacherPublicInfo(String subjectName) {
        return teacherRepository.findTeacherBySubjectNameContains(subjectName).map(teacherMapper::teacherPublicInfo);
    }
    Flux<FindAllTeacherStudents>findAllMyStudents(){
        return null;

    }

    private Mono<String> name() {
        return ReactiveSecurityContextHolder.getContext().map(SecurityContext::getAuthentication).map(Principal::getName);


    }
}



package org.example.student.grades;

import org.example.student.grades.dto.GradesRequest;
import org.example.student.grades.dto.GradesResponse;
import org.example.student.grades.dto.Teacher;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.security.Principal;

@Service
public class GradesServices {
    private final GradesRepository repository;
    private final GradesMapper mapper;
    private final WebClient webclient;

    public GradesServices(GradesRepository repository, GradesMapper mapper, WebClient.Builder webclient) {
        this.repository = repository;
        this.mapper = mapper;
        this.webclient = webclient.build();
    }

    Mono<GradesResponse> grade(GradesRequest gradesRequest) {
        return repository.findByEmailAndSubject(gradesRequest.email(), gradesRequest.subject()).flatMap(grades -> {
                    grades.setId(grades.getId());
                    grades.getGradesDescription().add(gradesRequest.gradesDescription() + " " + teacherByEmail());
                    return repository.save(grades);
                })

                .switchIfEmpty(teacherByEmail().flatMap(teacher->{
                    Grades grades = mapper.dtoToEntity(gradesRequest, teacher);
                  return repository.save(grades);})).map(mapper::entityToDto);

    }

    private Mono<String> teacherByEmail() {
        Mono<String> email = ReactiveSecurityContextHolder.getContext().map(SecurityContext::getAuthentication).map(Principal::getName);
        return email.flatMap(e -> webclient.get().uri("/teacher/private/{email}", e).retrieve().bodyToMono(Teacher.class).map(teacher -> teacher.firstName() + " " + teacher.lastName()));
    }



}


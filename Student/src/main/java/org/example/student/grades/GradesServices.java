package org.example.student.grades;

import org.example.student.grades.dto.*;
import org.example.student.security.token.TokenStore;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.security.Principal;
import java.time.LocalDate;
import java.time.ZoneId;

@Service
public class GradesServices {
    private final GradesRepository repository;
    private final GradesMapper mapper;
    private final WebClient webclient;
    private final TokenStore tokenStore;

    public GradesServices(GradesRepository repository, GradesMapper mapper, WebClient.Builder webclient, TokenStore tokenStore) {
        this.repository = repository;
        this.mapper = mapper;
        this.webclient = webclient.build();
        this.tokenStore = tokenStore;
    }

    Mono<GradesResponse> grade(GradesRequest gradesRequest) {
        ZoneId zone = ZoneId.of("Europe/Warsaw");
        LocalDate localDate = LocalDate.now(zone);

        return repository.findByEmailAndSubject(gradesRequest.email(), gradesRequest.subject()).flatMap(grades -> {

            grades.getGradesDescription().add(gradesRequest.gradesDescription() + "  " + localDate);
            return repository.save(grades).map(mapper::entityToDto);
        }).switchIfEmpty(teacherByEmail().flatMap(teacher -> {
            Grades grades = mapper.dtoToEntity(gradesRequest, teacher, localDate);
            return repository.save(grades);
        }).map(mapper::entityToDto));
    }

    Flux<StudentGrades> findOwnGrades() {
        return name().map(repository::findByEmail).flatMapMany(grades -> grades).map(mapper::studentGrades);
    }
    Flux<StudentGrades>gradesForTeacher(String studentEmail){
        return repository.findByEmail(studentEmail).map(mapper::gradesForTeacher);
    }


    private Mono<String> teacherByEmail() {

        String authorization = "Authorization";
        return name().flatMap(e -> webclient.get().uri("/teacher/private/{email}", e).header(authorization, "Bearer %s".formatted(tokenStore.getToken(e))).accept(MediaType.APPLICATION_JSON).retrieve().bodyToMono(Teacher.class).map(teacher -> teacher.firstName() + " " + teacher.lastName()));
    }

    private Mono<String> name() {
        return ReactiveSecurityContextHolder.getContext().map(SecurityContext::getAuthentication).map(Principal::getName);


    }


}


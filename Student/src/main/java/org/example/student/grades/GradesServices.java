package org.example.student.grades;

import org.example.student.grades.dto.GradesRequest;
import org.example.student.grades.dto.GradesResponse;
import org.example.student.grades.dto.Teacher;
import org.springframework.http.MediaType;
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

            grades.getGradesDescription().add(gradesRequest.gradesDescription());
            return repository.save(grades).map(mapper::entityToDto);
        }).switchIfEmpty(Mono.defer(() -> teacherByEmail().flatMap(teacher -> {

            Grades grades = mapper.dtoToEntity(gradesRequest, teacher);

            return repository.save(grades);
        }).map(mapper::entityToDto)));
    }


    private Mono<String> teacherByEmail() {
        String token = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZWFjaGVyLmJha0BpbnRlcmlvd3kucGwiLCJyb2xlcyI6WyJUZWFjaGVyIl0sImlhdCI6MTczMDg4Mjg5OSwiZXhwIjoxNzMxMDYyODk5fQ.J8udIyjdvR1JaO0qYU5W6cYgcv4EeZa0VaDTw4g_B88";
        String authorization = "Authorization";
        String header = "Bearer %s".formatted(token);
        Mono<String> email = ReactiveSecurityContextHolder.getContext().map(SecurityContext::getAuthentication).map(Principal::getName);
        return email.flatMap(e -> webclient.get().uri("/teacher/private/{email}", e).header(authorization, header).accept(MediaType.APPLICATION_JSON).retrieve().bodyToMono(Teacher.class).map(teacher -> teacher.firstName() + " " + teacher.lastName()));
    }


}


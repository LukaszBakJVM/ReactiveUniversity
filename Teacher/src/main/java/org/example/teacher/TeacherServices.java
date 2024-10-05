package org.example.teacher;

import org.example.teacher.dto.AddSchoolSubjects;
import org.example.teacher.dto.Subject;
import org.example.teacher.dto.TeacherInfo;
import org.example.teacher.dto.WriteNewTeacherDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class TeacherServices {

    private final TeacherMapper teacherMapper;
    private final TeacherRepository teacherRepository;
    private final WebClient.Builder webClientBuilder;
    @Value("${subject}")
    private String subjectUrl;
    @Value("${course}")
    private String courseUrl;
    @Value("${student}")
    private String studentUrl;

    public TeacherServices(TeacherMapper teacherMapper, TeacherRepository teacherRepository, WebClient.Builder webClientBuilder) {
        this.teacherMapper = teacherMapper;
        this.teacherRepository = teacherRepository;
        this.webClientBuilder = webClientBuilder;
    }

    Mono<WriteNewTeacherDto> createTeacher(WriteNewTeacherDto dto) {
        return teacherRepository.save(teacherMapper.dtoToEntity(dto)).map(teacherMapper::entityToDto);


    }

    Mono<AddSchoolSubjects> addSchoolSubjectsMono(List<String> schoolSubjectsMono, String email) {


        return subject(schoolSubjectsMono).flatMap(addSchoolSubjectsMono -> teacherRepository.findByEmail(email).flatMap(teacher -> {
            teacher.setId(teacher.getId());
            teacher.getSubjectName().addAll(schoolSubjectsMono);
            return teacherRepository.save(teacher);
        }).map(teacherMapper::addSchoolSubjects));

    }

    Mono<TeacherInfo> findTeacher(String email) {
        return teacherRepository.findByEmail(email).map(teacherMapper::teacherInfo);
    }

    Flux<TeacherInfo> allTeacherInfo() {
        return teacherRepository.findAll().map(teacherMapper::teacherInfo);
    }

    private Mono<List<String>> subject(List<String> subjectUris) {
        return Flux.fromIterable(subjectUris).flatMap(uri -> webClientBuilder.baseUrl(subjectUrl + "/subject/").build().get().uri(uri).retrieve().bodyToFlux(Subject.class).map(Subject::subject)).collectList();
    }

}



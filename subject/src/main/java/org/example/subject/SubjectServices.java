package org.example.subject;

import org.example.subject.dto.Course;
import org.example.subject.dto.SubjectDto;
import org.example.subject.exception.DuplicateSubjectException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class SubjectServices {
    private final WebClient.Builder webClient;
    private final SubjectRepository subjectRepository;
    private final SubjectMapper subjectMapper;

    @Value("${student}")
    private String studentUrl;
    @Value("${teacher}")
    private String teacherUrl;
    @Value("${course}")
    private String courseUrl;


    public SubjectServices(WebClient.Builder webClient, SubjectRepository subjectRepository, SubjectMapper subjectMapper) {
        this.webClient = webClient;
        this.subjectRepository = subjectRepository;

        this.subjectMapper = subjectMapper;

    }

    Mono<SubjectDto>createSubject(SubjectDto subjectDto) {
        return subjectRepository.findBySubject(subjectDto.subject()).flatMap(existingSubject -> Mono.<SubjectDto>error(new DuplicateSubjectException(String.format("Subject %s already exists", subjectDto.subject())))).switchIfEmpty(subjectRepository.save(subjectMapper.dtoToEntity(subjectDto)).map(subjectMapper::entityToDto));
    }


  Mono<List<Course>> findBySubject(String subject) {
        return webClient.baseUrl(courseUrl).build().get().uri("/course/{subject}/name",subject).retrieve().bodyToFlux(Course.class).collectList();

    }

}




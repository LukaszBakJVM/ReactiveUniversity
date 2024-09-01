package org.example.subject;

import org.example.subject.exception.DuplicateSubjectException;
import org.example.subject.exception.SubjectNotFoundException;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class SubjectServices {
    private final SubjectRepository subjectRepository;
    private final SubjectMapper subjectMapper;


    public SubjectServices(SubjectRepository subjectRepository, SubjectMapper subjectMapper) {
        this.subjectRepository = subjectRepository;

        this.subjectMapper = subjectMapper;
    }

    Mono<SubjectDto> newSubject(SubjectDto subjectDto) {
        return subjectRepository.findBySubject(subjectDto.subject()).flatMap(existingSubject -> Mono.<SubjectDto>error(new DuplicateSubjectException(String.format("Subject %s already exists",subjectDto.subject())))).switchIfEmpty(subjectRepository.save(subjectMapper.dtoToEntity(subjectDto)).map(subjectMapper::entityToDto));
    }

    Flux<SubjectDto> findAll() {
        return subjectRepository.findAll().map(subjectMapper::entityToDto);
    }

    Mono<SubjectDto> findBySubject(String subject) {
        return subjectRepository.findBySubject(subject).map(subjectMapper::entityToDto).switchIfEmpty(Mono.error(new SubjectNotFoundException(String.format("Subject %s not found", subject))));


    }
}




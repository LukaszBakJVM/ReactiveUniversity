package org.example.subject;

import org.example.subject.dto.SubjectDto;
import org.example.subject.exception.DuplicateSubjectException;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class SubjectServices {

    private final SubjectRepository subjectRepository;
    private final SubjectMapper subjectMapper;


    public SubjectServices(SubjectRepository subjectRepository, SubjectMapper subjectMapper) {

        this.subjectRepository = subjectRepository;

        this.subjectMapper = subjectMapper;

    }

    Mono<SubjectDto> createSubject(SubjectDto subjectDto) {
        return subjectRepository.findBySubject(subjectDto.subject()).flatMap(existingSubject -> Mono.<SubjectDto>error(new DuplicateSubjectException(String.format("Subject %s already exists", subjectDto.subject())))).switchIfEmpty(subjectRepository.save(subjectMapper.dtoToEntity(subjectDto)).map(subjectMapper::entityToDto));
    }


}




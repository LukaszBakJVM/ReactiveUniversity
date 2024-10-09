package org.example.subject;

import org.example.subject.dto.SubjectDto;
import org.springframework.stereotype.Service;

@Service
public class SubjectMapper {
    Subject dtoToEntity(SubjectDto dto) {
        return new Subject(dto.subject());
    }

    SubjectDto entityToDto(Subject subject) {
        return new SubjectDto(subject.subject());
    }
}

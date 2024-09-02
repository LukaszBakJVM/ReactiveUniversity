package org.example.teacher;

import jakarta.validation.ConstraintViolation;
import org.example.teacher.exception.ValidationException;
import org.springframework.stereotype.Service;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import reactor.core.publisher.Mono;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class TeacherServices {
    private final LocalValidatorFactoryBean validation;
    private final TeacherMapper teacherMapper;
    private final TeacherRepository teacherRepository;

    public TeacherServices(LocalValidatorFactoryBean validation, TeacherMapper teacherMapper, TeacherRepository teacherRepository) {
        this.validation = validation;
        this.teacherMapper = teacherMapper;
        this.teacherRepository = teacherRepository;
    }

    Mono<TeacherDto> newTeacher(TeacherDto dto) {
        Teacher teacher = teacherMapper.dtoToEntity(dto);
        validationTeacher(teacher);
        return teacherRepository.save(teacher).map(teacherMapper::entityToDto);


    }

    private void validationTeacher(Teacher teacher) {
        Set<ConstraintViolation<Teacher>> violations = validation.validate(teacher);

        if (!violations.isEmpty()) {
            String errorMessage = violations.stream().map(ConstraintViolation::getMessage).collect(Collectors.joining( " , "));

            throw new ValidationException(errorMessage);
        }
    }
}

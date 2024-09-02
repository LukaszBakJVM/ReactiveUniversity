package org.example.teacher;

import jakarta.validation.ConstraintViolation;
import org.example.teacher.dto.NewTeacherDto;
import org.example.teacher.dto.ResponseNewTeacherDto;
import org.example.teacher.exception.DuplicateEmailException;
import org.example.teacher.exception.ValidationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class TeacherServices {
    private final LocalValidatorFactoryBean validation;
    private final TeacherMapper teacherMapper;
    private final TeacherRepository teacherRepository;
    private final WebClient.Builder webClientBuilder;
    @Value("${subject}")
    private String subjectUrl;
    @Value("${course}")
    private String courseUrl;
    @Value("${student}")
    private String studentUrl;

    public TeacherServices(LocalValidatorFactoryBean validation, TeacherMapper teacherMapper, TeacherRepository teacherRepository, WebClient.Builder webClientBuilder) {
        this.validation = validation;
        this.teacherMapper = teacherMapper;
        this.teacherRepository = teacherRepository;
        this.webClientBuilder = webClientBuilder;
    }

    Mono<ResponseNewTeacherDto> createTeacher(NewTeacherDto dto) {
        Teacher teacher = teacherMapper.dtoToEntity(dto);
        validationTeacher(teacher);
        return teacherRepository.findByEmail(dto.email()).flatMap(existingEmail -> Mono.<ResponseNewTeacherDto>error(new DuplicateEmailException(String.format("Email %s already exists", dto.email())))).switchIfEmpty(teacherRepository.save(teacherMapper.dtoToEntity(dto)).map(teacherMapper::entityToDto));


    }

    private void validationTeacher(Teacher teacher) {
        Set<ConstraintViolation<Teacher>> violations = validation.validate(teacher);

        if (!violations.isEmpty()) {
            String errorMessage = violations.stream().map(ConstraintViolation::getMessage).collect(Collectors.joining(" , "));

            throw new ValidationException(errorMessage);
        }
    }
}

package org.example.student;

import org.example.student.dto.WriteNewPerson;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class StudentServices {
    @Value("${teacher}")
    private String teacher;
    @Value("${course}")
    private String courseUrl;
    @Value("${student}")
    private String studentUrl;

    private final StudentMapper studentMapper;
    private final StudentRepository studentRepository;
    private final WebClient.Builder webClientBuilder;

    public StudentServices(StudentMapper studentMapper, StudentRepository studentRepository, WebClient.Builder webClientBuilder) {
        this.studentMapper = studentMapper;
        this.studentRepository = studentRepository;
        this.webClientBuilder = webClientBuilder;
    }
    Mono<WriteNewPerson>createStudent(WriteNewPerson person) {
        return studentRepository.save(studentMapper.dtoToStudent(person)).map(studentMapper::studentToDto);
    }
}

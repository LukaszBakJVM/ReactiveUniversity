package org.example.teacher;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.example.teacher.dto.*;
import org.example.teacher.exception.ConnectionException;
import org.example.teacher.exception.UsernameNotFoundException;
import org.example.teacher.exception.WrongCredentialsException;
import org.example.teacher.security.token.TokenStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.security.Principal;
import java.util.List;

@Service
public class TeacherServices {

    private final TeacherMapper teacherMapper;
    private final TeacherRepository teacherRepository;
    private final WebClient.Builder webClient;
    private final TokenStore tokenStore;
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final Logger logger = LoggerFactory.getLogger(TeacherServices.class);
    @Value("${student}")
    private String studentUrl;
    @Value("${course}")
    private String courseUrl;


    public TeacherServices(TeacherMapper teacherMapper, TeacherRepository teacherRepository, WebClient.Builder webClient, TokenStore tokenStore, KafkaTemplate<String, Object> kafkaTemplate) {
        this.teacherMapper = teacherMapper;
        this.teacherRepository = teacherRepository;
        this.webClient = webClient;
        this.tokenStore = tokenStore;
        this.kafkaTemplate = kafkaTemplate;
    }


    Mono<AddSchoolSubjects> addSchoolSubjects(AddSchoolSubjects schoolSubjects) {
        return teacherRepository.findByEmail(schoolSubjects.email()).flatMap(teacher -> {
            teacher.setId(teacher.getId());
            teacher.getSubjectName().addAll(schoolSubjects.subjects());
            return teacherRepository.save(teacher);
        }).map(teacherMapper::addSchoolSubjectsToDto).switchIfEmpty(Mono.error(new UsernameNotFoundException(String.format("Teacher %s not found", schoolSubjects.email()))));

    }

    Mono<TeacherPrivateInfo> findTeacher(String email) {
        return teacherRepository.findByEmail(email).map(teacherMapper::teacherPrivateInfo);
    }

    Flux<TeacherPrivateInfo> allTeacherInfo() {
        return teacherRepository.findAll().map(teacherMapper::teacherPrivateInfo);
    }

    Mono<TeacherPublicInfo> teacherPublicInfo(String subjectName) {
        return teacherRepository.findTeacherBySubjectNameContains(subjectName).map(teacherMapper::teacherPublicInfo);
    }

    Flux<FindAllTeacherStudents> findAllMyStudents() {
        return name().flatMap(teacherRepository::findByEmail).map(teacherMapper::email).flatMapIterable(TeacherSubjects::subjects).flatMap(this::findCourseBySubject).flatMap(this::finaAllUniqueStudents).distinct().flatMap(e -> allGrades(e.email()).map(grades -> new FindAllTeacherStudents(new Student(e.firstName(), e.lastName(), e.email(), e.course()), grades)));


    }

    private Flux<String> findCourseBySubject(String subject) {
        return webClient.baseUrl(courseUrl).build().get().uri("/course/{subject}/name", subject).accept(MediaType.APPLICATION_JSON).retrieve().bodyToFlux(CourseName.class).map(CourseName::courseName).onErrorResume(WebClientRequestException.class, response -> Mono.error(new ConnectionException("Connection refused : course")));
    }

    private Flux<Student> finaAllUniqueStudents(String course) {
        return webClient.baseUrl(studentUrl).build().get().uri("student/studentInfo/{course}", course).accept(MediaType.APPLICATION_JSON).retrieve().bodyToFlux(Student.class).onErrorResume(WebClientRequestException.class, response -> Mono.error(new ConnectionException("Connection refused : student")));
    }

    Mono<List<Grades>> allGrades(String email) {
        String authorization = "Authorization";
        return name().flatMap(n -> webClient.baseUrl(studentUrl).build().get().uri("/grades/{email}", email).header(authorization, "Bearer %s".formatted(tokenStore.getToken(n))).accept(MediaType.APPLICATION_JSON).retrieve().onStatus(HttpStatusCode::is4xxClientError, response -> Mono.error(new WrongCredentialsException("Wrong credentials"))).bodyToFlux(Grades.class).collectList()).onErrorResume(WebClientRequestException.class, response -> Mono.error(new ConnectionException("Connection refused : grades")));
    }

    private Mono<String> name() {
        return ReactiveSecurityContextHolder.getContext().map(SecurityContext::getAuthentication).map(Principal::getName);


    }

    @KafkaListener(topics = "teacher-topic", groupId = "your-consumer-group")
    private void listenTeacherTopic(ConsumerRecord<String, String> record) {
        String topicKey = "WriteNewPerson";
        String key = record.key();
        if (key.equals(topicKey)) {
            logger.info("Received Person: {}", record.value());
            WriteNewTeacherDto deserialization = deserialization(record.value());
            Teacher teacher = teacherMapper.dtoToEntity(deserialization);
            teacherRepository.save(teacher).subscribe();
            sendMessage("response", teacher).subscribe();
            logger.info("send to registration {}", teacher);
        } else {
            logger.info("my key  {} , received key {}", topicKey, key);
        }

    }

    private Mono<Void> sendMessage(String topic, Object body) {
        return Mono.fromFuture(() -> kafkaTemplate.send(topic, body)).then();
    }

    private WriteNewTeacherDto deserialization(String json) {
        ObjectMapper obj = new ObjectMapper();
        try {
            return obj.readValue(json, WriteNewTeacherDto.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);

        }
    }


}



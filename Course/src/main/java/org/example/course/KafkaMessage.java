package org.example.course;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.example.course.dto.SubjectsCourseDto;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import reactor.core.publisher.Mono;

import java.util.Set;

public class KafkaMessage {
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final CourseRepository repository;
    private final CourseMapper mapper;

    public KafkaMessage(KafkaTemplate<String, Object> kafkaTemplate, CourseRepository repository, CourseMapper mapper) {
        this.kafkaTemplate = kafkaTemplate;
        this.repository = repository;
        this.mapper = mapper;
    }


    @KafkaListener(topics = "subjects-topic", groupId = "your-consumer-group")
   void listenSubjectTopic(ConsumerRecord<String, String> record) {
        String topicKey = "setSubjects";
        if (record.key().equals(topicKey)){
        //     deserialization(record.value()).subjects().stream().map(repository::findBySubject).flatMap(e -> e.flux().toStream().flatMap(SubjectDto::subject));


        }


    }
    private Mono<Void> sendMessage(String topic, String key, Object body) {
        return Mono.fromFuture(() -> kafkaTemplate.send(topic, key, body)).then();
    }

    private Set<String>  deserialization(String json) {
        ObjectMapper obj = new ObjectMapper();
        try {
            return obj.readValue(json, SubjectsCourseDto.class).subjects();

        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);

        }


    }

}

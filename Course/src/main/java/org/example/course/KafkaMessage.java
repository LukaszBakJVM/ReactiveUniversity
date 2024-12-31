package org.example.course;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.example.course.dto.SubjectsCourseDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
@Service
public class KafkaMessage {
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final CourseRepository repository;
    private final CourseMapper mapper;
    private final Logger logger = LoggerFactory.getLogger(KafkaMessage.class);

    public KafkaMessage(KafkaTemplate<String, Object> kafkaTemplate, CourseRepository repository, CourseMapper mapper) {
        this.kafkaTemplate = kafkaTemplate;
        this.repository = repository;
        this.mapper = mapper;
    }


    @KafkaListener(topics = "subjects-topic", groupId = "your-consumer-group")
   void listenSubjectTopic(ConsumerRecord<String, String> record) {
        String topicKey = "setSubjects";
        if (record.key().equals(topicKey)){
            Set<String> deserialization = deserialization(record.value());





        }


    }
    private Mono<Void> sendMessage(String topic, String key, Object body) {
        return Mono.fromFuture(() -> kafkaTemplate.send(topic, key, body)).then();
    }

    private Set<String>  deserialization(String json) {

        ObjectMapper obj = new ObjectMapper();
        try {

            Set<String> subjects  = obj.readValue(json, HashSet.class);
            logger.info(subjects.toString());
            return subjects;

        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);

        }


    }

}

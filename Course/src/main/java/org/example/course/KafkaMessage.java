package org.example.course;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.HashSet;
import java.util.Set;

@Service
public class KafkaMessage {
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final CourseRepository repository;

    private final Logger logger = LoggerFactory.getLogger(KafkaMessage.class);

    public KafkaMessage(KafkaTemplate<String, Object> kafkaTemplate, CourseRepository repository) {
        this.kafkaTemplate = kafkaTemplate;
        this.repository = repository;

    }


    @KafkaListener(topics = "subjects-topic", groupId = "your-consumer-group")
    private void listenSubjectTopic(ConsumerRecord<String, String> consumer) {
        String topicKey = "setSubjects";
        if (consumer.key().equals(topicKey)) {
            Flux.fromIterable(deserialization(consumer.value())).flatMap(repository::findCourseBySubjectNameContaining).map(Course::getCourseName).collectList().flatMap(s -> sendMessage("course-topic", "course", s)).subscribe();


        } else {
            logger.debug("{}", consumer.key());
        }


    }

    private Mono<Void> sendMessage(String topic, String key, Object body) {
        return Mono.fromFuture(() -> kafkaTemplate.send(topic, key, body)).then();
    }

    private Set<String> deserialization(String json) {

        ObjectMapper obj = new ObjectMapper();
        try {

            return obj.readValue(json, HashSet.class);


        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);

        }


    }

}

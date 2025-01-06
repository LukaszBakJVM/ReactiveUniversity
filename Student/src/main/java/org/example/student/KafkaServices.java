package org.example.student;

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
public class KafkaServices {
    private final StudentRepository repository;
    private final StudentMapper mapper;
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final Logger logger = LoggerFactory.getLogger(KafkaServices.class);

    public KafkaServices(StudentRepository repository, StudentMapper mapper, KafkaTemplate<String, Object> kafkaTemplate) {
        this.repository = repository;
        this.mapper = mapper;
        this.kafkaTemplate = kafkaTemplate;
    }

    @KafkaListener(topics = "course-topic", groupId = "your-consumer-group")
    private void listenSubjectTopic(ConsumerRecord<String, String> consumer) {
        String topicKey = "course";
        if (consumer.key().equals(topicKey)) {
            Flux.fromIterable(deserialization(consumer.value())).flatMap(repository::findByCourse).map(mapper::studentToDto).collectList().flatMap(s -> sendMessage("student-topic", "student", s)).subscribe();


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

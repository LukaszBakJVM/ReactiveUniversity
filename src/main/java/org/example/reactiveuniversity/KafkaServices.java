package org.example.reactiveuniversity;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.example.reactiveuniversity.dto.WriteNewPerson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class KafkaServices {

    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final Logger logger = LoggerFactory.getLogger(KafkaServices.class);


    public KafkaServices(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public Mono<Void> sendMessage(String topic, String key, Object body) {
        ProducerRecord<String, Object> record = new ProducerRecord<>(topic, null, key, body);
        return Mono.fromFuture(() -> kafkaTemplate.send(record)).then();
    }

    @KafkaListener(topics = "response", groupId = "your-consumer-group")
    private void listenResponseTopic(ConsumerRecord<String, String> record) {
        String topicKey = "WritePerson";
        String key = record.key();
        if (key.equals(topicKey)) {
            WriteNewPerson person = deserialization(record.value());
            logger.info("Received Person: {}", person);

        } else {
            logger.info("Received key: {}", record.key());
        }
    }

    private WriteNewPerson deserialization(String json) {
        ObjectMapper obj = new ObjectMapper();
        try {
            return obj.readValue(json, WriteNewPerson.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);

        }
    }


}

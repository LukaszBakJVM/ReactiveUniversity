package org.example.reactiveuniversity;


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
   WriteNewPerson writeNewPerson;
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final Logger logger = LoggerFactory.getLogger(KafkaServices.class);


    public KafkaServices(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public Mono<Void> sendMessage(String topic, String key,Object body) {
        ProducerRecord<String, Object> record = new ProducerRecord<>(topic, null, key, body);
        return Mono.fromFuture(()-> kafkaTemplate.send(record)).then();
    }

    @KafkaListener(topics = "response", groupId = "your-consumer-group")
    private void listenResponseTopic(WriteNewPerson person) {
        writeNewPerson = person;
        logger.info("Received Person: {}", person);
    }


}

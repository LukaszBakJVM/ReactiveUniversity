package org.example.reactiveuniversity.appconfig;


import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class KafkaProducerServices {
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public KafkaProducerServices(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public Mono<Void> sendMessage(String topic, Object body, String token) {

        String header = "Bearer %s".formatted(token);


        var kafkaMessage = MessageBuilder.withPayload(body).setHeader("Authorization", header).build();


        return Mono.fromFuture(() -> kafkaTemplate.send(topic, kafkaMessage)).then();
    }
}

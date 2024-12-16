package org.example.reactiveuniversity.appconfig;


import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

@Service
public class KafkaProducerServices {
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public KafkaProducerServices(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }


    public void sendMessage(String topic, Object body, String token) {

        Message<Object> kafkaMessage = MessageBuilder.withPayload(body).setHeader("Authorization", token).build();

        kafkaTemplate.send(topic, kafkaMessage.getPayload());
    }
}

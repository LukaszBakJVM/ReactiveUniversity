package org.example.reactiveuniversity.appconfig;


import org.example.reactiveuniversity.dto.WriteNewPerson;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

@Service
public class KafkaProducerServices {
    private final KafkaTemplate<String, WriteNewPerson> kafkaTemplate;

    public KafkaProducerServices(KafkaTemplate<String, WriteNewPerson> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }


    public void sendMessage(String topic, WriteNewPerson body, String token) {

        Message<WriteNewPerson> kafkaMessage = MessageBuilder.withPayload(body).setHeader("Authorization", token).build();

        kafkaTemplate.send(topic, kafkaMessage.getPayload());
    }
}

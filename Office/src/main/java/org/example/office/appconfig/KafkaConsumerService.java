package org.example.office.appconfig;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumerService {
    @KafkaListener(topics = "office-topic", groupId = "your-consumer-group")
    public void listenStudentTopic(Object person) {

        System.out.println("Received Person: " + person);
    }
}

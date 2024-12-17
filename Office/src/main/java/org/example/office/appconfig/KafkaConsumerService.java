package org.example.office.appconfig;

import org.example.office.dto.WriteNewPersonOffice;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumerService {
    @KafkaListener(topics = "office-topic", groupId = "your-consumer-group")
    public void listenStudentTopic(WriteNewPersonOffice person) {
        // Przetwarza wiadomość jako obiekt Person
        System.out.println("Received Person: " + person);
    }
}

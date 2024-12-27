package org.example.office;


import org.example.office.dto.WriteNewPersonOffice;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class OfficeServices {


    private final OfficeRepository officeRepository;
    private final OfficeMapper officeMapper;
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final Logger logger = LoggerFactory.getLogger(OfficeServices.class);


    public OfficeServices(OfficeRepository officeRepository, OfficeMapper officeMapper, KafkaTemplate<String, Object> kafkaTemplate) {

        this.officeRepository = officeRepository;
        this.officeMapper = officeMapper;
        this.kafkaTemplate = kafkaTemplate;
    }

    Mono<WriteNewPersonOffice> createNewPerson(WriteNewPersonOffice dto) {
        return officeRepository.save(officeMapper.dtoToOffice(dto)).map(officeMapper::officeToDto);
    }

    @KafkaListener(topics = "office-topic", groupId = "your-consumer-group")
    private void listenStudentTopic(WriteNewPersonOffice person) {
        logger.info("Received Person: {}", person);
        Office office = officeMapper.dtoToOffice(person);
        officeRepository.save(office).subscribe();
        String TOPIC = "response";
        sendMessage(TOPIC, person).subscribe();
        logger.info("send to registration {}",person);

    }

    private Mono<Void> sendMessage(String topic, Object body) {
        return Mono.fromFuture(() -> kafkaTemplate.send(topic, body)).then();
    }


}

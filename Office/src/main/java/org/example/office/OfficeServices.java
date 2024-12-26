package org.example.office;



import org.example.office.dto.WriteNewPersonOffice;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class OfficeServices {


    private final OfficeRepository officeRepository;
    private final OfficeMapper officeMapper;
    private final Logger logger = LoggerFactory.getLogger(OfficeServices.class);


    public OfficeServices(OfficeRepository officeRepository, OfficeMapper officeMapper) {

        this.officeRepository = officeRepository;
        this.officeMapper = officeMapper;

    }

    Mono<WriteNewPersonOffice> createNewPerson(WriteNewPersonOffice dto) {
        return officeRepository.save(officeMapper.dtoToOffice(dto)).map(officeMapper::officeToDto);
    }

    @KafkaListener(topics = "office-topic", groupId = "your-consumer-group")
    public void listenStudentTopic(WriteNewPersonOffice person) {
        logger.info("Received Person: {}",person);


        Office office = officeMapper.dtoToOffice(person);
        officeRepository.save(office).subscribe();




    }


}

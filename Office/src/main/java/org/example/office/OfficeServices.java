package org.example.office;


import org.example.office.dto.WriteNewPersonOffice;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class OfficeServices {


    private final OfficeRepository officeRepository;
    private final OfficeMapper officeMapper;


    public OfficeServices(OfficeRepository officeRepository, OfficeMapper officeMapper) {

        this.officeRepository = officeRepository;
        this.officeMapper = officeMapper;

    }

    Mono<WriteNewPersonOffice> createNewPerson(WriteNewPersonOffice dto) {
        return officeRepository.save(officeMapper.dtoToOffice(dto)).map(officeMapper::officeToDto);
    }


}

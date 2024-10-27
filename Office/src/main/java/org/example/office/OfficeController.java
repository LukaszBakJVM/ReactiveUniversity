package org.example.office;


import org.example.office.dto.ResponseOk;
import org.example.office.dto.WriteNewPersonOffice;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/office")
public class OfficeController {
    private final OfficeServices officeServices;

    public OfficeController(OfficeServices officeServices) {
        this.officeServices = officeServices;
    }

    @PostMapping
    ResponseEntity<ResponseOk> createNewPerson(@RequestBody WriteNewPersonOffice dto) {
      officeServices.createNewPerson(dto).subscribe();
        return ResponseEntity.ok(new ResponseOk("Created"));
    }


}

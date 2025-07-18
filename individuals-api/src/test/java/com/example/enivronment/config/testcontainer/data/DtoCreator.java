package com.example.enivronment.config.testcontainer.data;

import java.util.List;

import org.springframework.stereotype.Component;

import com.example.individual.dto.AddressDto;
import com.example.individual.dto.AddressWriteDto;
import com.example.individual.dto.IndividualDto;
import com.example.individual.dto.IndividualPageDto;
import com.example.individual.dto.IndividualWriteDto;
import com.example.individual.dto.UserLoginRequest;

import static com.example.spec.integration.LifecycleSpecification.PERSON_EMAIL;

@Component
public class DtoCreator {

    public IndividualPageDto buildIndividualPageDto() {
        return new IndividualPageDto().items(List.of(buildIndividualDto()));
    }

    public IndividualDto buildIndividualDto() {
        var address = new AddressDto();
        address.setAddress("Minskaya 15, d.8, kv.5");
        address.setZipCode("210009");
        address.setCity("Vitebsk");
        address.setCountryCode("BLR");

        var dto = new IndividualDto();
        dto.setFirstName("Vlad");
        dto.setLastName("Kaliaha");
        dto.setEmail(PERSON_EMAIL);
        dto.passportNumber("BY24003");
        dto.phoneNumber("+375298082919");
        dto.setAddress(address);

        return dto;
    }

    public IndividualWriteDto buildIndividualWriteDto() {
        var address = new AddressWriteDto();
        address.setAddress("Minskaya 15, d.8, kv.5");
        address.setZipCode("210009");
        address.setCity("Vitebsk");
        address.setCountryCode("BLR");

        var request = new IndividualWriteDto();
        request.setFirstName("Vlad");
        request.setLastName("Kaliaha");
        request.setEmail(PERSON_EMAIL);
        request.setPassword("password");
        request.setConfirmPassword("password");
        request.passportNumber("BY24003");
        request.phoneNumber("+375298082919");
        request.setAddress(address);

        return request;
    }

    public UserLoginRequest buildUserLoginRequest() {
        var request = new UserLoginRequest();
        request.setEmail(PERSON_EMAIL);
        request.setPassword("password");
        return request;
    }

    public IndividualWriteDto buildForUpdate() {
        var individualWriteDto = buildIndividualWriteDto();
        individualWriteDto.setFirstName("Vlad-update");
        individualWriteDto.setLastName("Kaliaha-update");
        individualWriteDto.passportNumber("BY24003-update");
        individualWriteDto.phoneNumber("+375298082919-update");

        var address = buildIndividualWriteDto().getAddress();
        address.setAddress("Minskaya 15, d.8, kv.5-update");
        address.setZipCode("210009-update");
        address.setCity("Vitebsk-update");
        individualWriteDto.setAddress(address);
        return individualWriteDto;
    }
}

package ru.itmo.communal.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.itmo.communal.controller.dto.SubmitMeterReadingRequest;
import ru.itmo.communal.entity.MeterReading;
import ru.itmo.communal.repository.MeterReadingRepository;
import ru.itmo.communal.repository.PublicUtilityRepository;
import ru.itmo.communal.repository.SubscriberAddressRepository;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/meter-reading")
@CrossOrigin("*")
public class MeterReadingController {
    @Autowired
    MeterReadingRepository meterReadingRepository;

    @Autowired
    PublicUtilityRepository publicUtilityRepository;

    @Autowired
    SubscriberAddressRepository subscriberAddressRepository;

    @PostMapping
    public void submitMeterReadings(@RequestBody SubmitMeterReadingRequest meterReadingRequest) {
        MeterReading meterReading = MeterReading.builder()
                .value(meterReadingRequest.getValue())
                .utility(publicUtilityRepository.findById(meterReadingRequest.getUtilityId()).orElseThrow())
                .datetime(LocalDateTime.now())
                .address(subscriberAddressRepository.findById(meterReadingRequest.getAddressId()).orElseThrow())
                .build();
        meterReadingRepository.save(
                meterReading
        );
    }
}

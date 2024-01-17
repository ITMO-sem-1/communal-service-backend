package ru.itmo.communal.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.itmo.communal.controller.dto.SubmitMeterReadingRequest;
import ru.itmo.communal.entity.MeterReading;
import ru.itmo.communal.entity.PublicUtility;
import ru.itmo.communal.entity.SubscriberAddress;
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

    @GetMapping("/last/{addrId}/{utilId}")
    public ResponseEntity<?> getLaseMeterReadings(@PathVariable Integer addrId, @PathVariable Integer utilId) {
        SubscriberAddress address = subscriberAddressRepository.findById(addrId).orElseThrow(() -> new IllegalStateException("Несуществующий адрес"));
        PublicUtility utility = publicUtilityRepository.findById(utilId).orElseThrow(() -> new IllegalStateException("Несуществующая услуга"));
        MeterReading mr = meterReadingRepository.findFirstByAddressAndUtilityOrderByDatetimeDesc(address, utility).orElseThrow(() -> new IllegalStateException("нет ни одного показания"));
        return ResponseEntity.ok().body(SubmitMeterReadingRequest.builder()
                .addressId(addrId)
                .utilityId(utilId)
                .value(mr.getValue())
                .build());
    }
}

package ru.itmo.communal.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.itmo.communal.controller.dto.BillReportFilters;
import ru.itmo.communal.entity.Receipt;
import ru.itmo.communal.entity.SubscriberAddress;
import ru.itmo.communal.repository.ReceiptRepository;
import ru.itmo.communal.repository.SubscriberAddressRepository;
import ru.itmo.communal.service.TotalCalculationService;

import java.util.List;

@RestController
@RequestMapping("/report")
@RequiredArgsConstructor
@CrossOrigin("*")
public class ReportController {

    @Autowired
    private TotalCalculationService totalCalculationService;

    @Autowired
    private ReceiptRepository receiptRepository;

    @Autowired
    private SubscriberAddressRepository subscriberAddressRepository;

    @GetMapping
    public void generateReports() {
        totalCalculationService.calculate();
    }


    @PostMapping("/bills")
    public List<Receipt> getAddressReceipts(@RequestBody BillReportFilters filters) {
        SubscriberAddress address = subscriberAddressRepository.findById(filters.getAddressId()).orElseThrow();
        return receiptRepository.findAllByDateTimeGreaterThanEqualAndDateTimeLessThanAndSubscriberAddress(filters.getFrom(), filters.getTo(), address);
    }
}

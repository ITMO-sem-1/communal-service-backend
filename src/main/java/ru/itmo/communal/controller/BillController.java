package ru.itmo.communal.controller;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.itmo.communal.controller.dto.BillResponse;
import ru.itmo.communal.controller.dto.UtilsCalcResponse;
import ru.itmo.communal.entity.Receipt;
import ru.itmo.communal.repository.ReceiptRepository;
import ru.itmo.communal.repository.SubscriberAddressRepository;
import ru.itmo.communal.repository.TotalCalculationsRepository;

@RestController
@AllArgsConstructor
@RequestMapping("/bill")
public class BillController {

    @Autowired
    private final ReceiptRepository receiptRepository;

    @Autowired
    private final SubscriberAddressRepository subscriberAddressRepository;

    @Autowired
    private final TotalCalculationsRepository totalCalculationsRepository;
    @RequestMapping("/{address_id}")
    BillResponse getBill(@PathVariable Integer address_id) {
        Receipt bill = receiptRepository.findFirstBySubscriberAddressOrderByDateTimeDesc(
                subscriberAddressRepository.findById(address_id).orElseThrow()
        );
        Long debt = receiptRepository.findAllByPaidAndSubscriberAddress(false, subscriberAddressRepository.findById(address_id).orElseThrow()).stream().map(Receipt::getSum).reduce(0L, (a, b) -> a + b);
        return BillResponse
                .builder()
                .date(bill.getDateTime())
                .id(bill.getId())
                .addrId(bill.getSubscriberAddress().getId())
                .utils(
                        bill.getUtilityCalculations().stream().map(
                                t -> UtilsCalcResponse
                                        .builder()
                                        .utilName(t.getUtility().getName())
                                        .utilPrice(t.getSum())
                                        .build()).toList()
                )
                .credit(debt)
                .build();
    }
}

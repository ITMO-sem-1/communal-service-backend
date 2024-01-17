package ru.itmo.communal.controller;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.itmo.communal.controller.dto.BillResponse;
import ru.itmo.communal.controller.dto.UtilsCalcResponse;
import ru.itmo.communal.entity.Receipt;
import ru.itmo.communal.repository.ReceiptRepository;
import ru.itmo.communal.repository.SubscriberAddressRepository;
import ru.itmo.communal.repository.TotalCalculationsRepository;

import java.util.List;

@CrossOrigin("*")
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
    @GetMapping("/{address_id}")
    BillResponse getBill(@PathVariable Integer address_id) {
        Receipt bill = receiptRepository.findFirstBySubscriberAddressOrderByDateTimeDesc(
                subscriberAddressRepository.findById(address_id).orElseThrow()
        );
        Long debt = receiptRepository.findAllByPaidAndSubscriberAddress(false, subscriberAddressRepository.findById(address_id).orElseThrow()).stream().map(Receipt::getSum).reduce(0L, (a, b) -> a + b);
        if (!bill.isPaid()) {
            debt = debt - bill.getSum();
        }
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
                .isPaid(bill.isPaid())
                .build();
    }


    @PostMapping("/{bill_id}")
    void payBIll(@PathVariable Integer bill_id) {
        Receipt bill = receiptRepository.findById(bill_id).orElseThrow();
        List<Receipt> unpaidBills = receiptRepository.findAllByPaidAndSubscriberAddress(false, bill.getSubscriberAddress());
        for (Receipt unpaid_bill : unpaidBills) {
            unpaid_bill.setPaid(true);
            receiptRepository.save(unpaid_bill);
        }
    }

}

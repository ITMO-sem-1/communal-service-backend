package ru.itmo.communal.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.itmo.communal.entity.*;
import ru.itmo.communal.repository.*;

import java.time.LocalDateTime;
import java.time.Month;
import java.time.Year;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TotalCalculationService {
    private final SubscriberAddressRepository subscriberAddressRepository;
    private final MeterReadingRepository meterReadingRepository;
    private final TotalCalculationsRepository totalCalculationsRepository;
    private final TariffUtilityRelationRepository tariffUtilityRelationRepository;
    private final ReceiptRepository receiptRepository;

    public void calculate() {
        LocalDateTime from, to;
        from = LocalDateTime.now().withDayOfMonth(1).withHour(0).withMinute(0);
        to = LocalDateTime.now().withDayOfMonth(Month.of(from.getMonthValue()).length(Year.isLeap(from.getYear()))).withHour(0).withMinute(0);
        List<SubscriberAddress> addresses = subscriberAddressRepository.findAll();
        for (SubscriberAddress address : addresses) {
            Tariff tariff = address.getTariff();
            List<TotalCalculations> newCalculations = new ArrayList<>();
            for (PublicUtility utility : address.getUtilitiesEnabled()) {
                TotalCalculations prevCalc = totalCalculationsRepository.findFirstBySubscriberAddressAndUtilityOrderByDatetimeDesc(address, utility);
                MeterReading nowCalcMeterReading = meterReadingRepository
                        .getLastMeterReadingForAddress(
                                address,
                                utility,
                                from,
                                to
                        );
                if (prevCalc == null) {
                    long price = tariffUtilityRelationRepository.findFirstByTariffAndUtility(tariff, utility).getPrice();
                    newCalculations.add(
                            totalCalculationsRepository.save(
                                    TotalCalculations
                                            .builder()
                                            .utility(utility)
                                            .calculationType(CalculationType.METER_READING_BASED)
                                            .datetime(LocalDateTime.now())
                                            .meterReading(nowCalcMeterReading)
                                            .subscriberAddress(address)
                                            .sum((nowCalcMeterReading.getValue() - 0) * price)
                                            .build()
                            )
                    );
                    continue;
                }
                if (prevCalc.getCalculationType() == CalculationType.METER_READING_BASED) {
                    MeterReading prevCalcMeterReading = prevCalc.getMeterReading();

                    if (nowCalcMeterReading == null) {
                        newCalculations.add(
                                totalCalculationsRepository.save(
                                        TotalCalculations
                                                .builder()
                                                .utility(utility)
                                                .calculationType(CalculationType.MEAN)
                                                .datetime(LocalDateTime.now())
                                                .subscriberAddress(address)
                                                .sum(prevCalc.getSum())
                                                .build())
                        );
                    } else {
                        long price = tariffUtilityRelationRepository.findFirstByTariffAndUtility(tariff, utility).getPrice();
                        newCalculations.add(
                                totalCalculationsRepository.save(
                                        TotalCalculations
                                                .builder()
                                                .utility(utility)
                                                .calculationType(CalculationType.METER_READING_BASED)
                                                .datetime(LocalDateTime.now())
                                                .meterReading(nowCalcMeterReading)
                                                .subscriberAddress(address)
                                                .sum((nowCalcMeterReading.getValue() - prevCalcMeterReading.getValue()) * price)
                                                .build())
                        );
                    }
                }

            }
            receiptRepository.save(
                    Receipt
                            .builder()
                            .utilityCalculations(newCalculations)
                            .sum(
                                    newCalculations
                                            .stream()
                                            .map(
                                                    TotalCalculations::getSum
                                            )
                                            .reduce(0L, Long::sum)
                            )
                            .paid(false)
                            .subscriberAddress(address)
                            .dateTime(LocalDateTime.now())
                            .build()
            );
        }
    }
}

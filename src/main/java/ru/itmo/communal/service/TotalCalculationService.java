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
        var lastRecipt = receiptRepository.findFirstByOrderByDateTimeDesc();
        from = lastRecipt.isPresent()? lastRecipt.get().getDateTime() : LocalDateTime.now().withDayOfMonth(1).withHour(0).withMinute(0);
        //from = LocalDateTime.now().withDayOfMonth(1).withHour(0).withMinute(0);
        to = LocalDateTime.now().withDayOfMonth(Month.of(from.getMonthValue()).length(Year.isLeap(from.getYear()))).withHour(0).withMinute(0);
        //TODO for demo
        from = lastRecipt.isPresent()? lastRecipt.get().getDateTime() : LocalDateTime.now().withMonth(1).withDayOfMonth(1).withHour(0).withMinute(0);
        to = LocalDateTime.now();
        ////TODO...
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
                    double price = tariffUtilityRelationRepository.findFirstByTariffAndUtility(tariff, utility).getPrice();
                    if (nowCalcMeterReading == null) {
                        newCalculations.add(
                                totalCalculationsRepository.save(
                                        TotalCalculations
                                                .builder()
                                                .utility(utility)
                                                .calculationType(CalculationType.MEAN)
                                                .datetime(LocalDateTime.now())
                                                .meterReading(nowCalcMeterReading)
                                                .subscriberAddress(address)
                                                .sum(((nowCalcMeterReading == null ? 0.0: nowCalcMeterReading.getValue()) - 0) * price)
                                                .build()
                                )
                        );
                    }
                    else {
                        newCalculations.add(
                                totalCalculationsRepository.save(
                                        TotalCalculations
                                                .builder()
                                                .utility(utility)
                                                .calculationType(CalculationType.METER_READING_BASED)
                                                .datetime(LocalDateTime.now())
                                                .meterReading(nowCalcMeterReading)
                                                .subscriberAddress(address)
                                                .sum(((nowCalcMeterReading == null ? 0.0: nowCalcMeterReading.getValue()) - 0) * price)
                                                .build()
                                )
                        );
                    }
                    continue;
                }
                // TODO hack for demo tests
                if (prevCalc.getCalculationType() == CalculationType.METER_READING_BASED || true) {
                    MeterReading prevCalcMeterReading = prevCalc.getMeterReading();

                    if (nowCalcMeterReading == null) {
                        double price = tariffUtilityRelationRepository.findFirstByTariffAndUtility(tariff, utility).getPrice();
                        List<TotalCalculations> allPrev = totalCalculationsRepository.findBySubscriberAddressAndUtilityOrderByDatetimeDesc(address, utility);
                        double mean = allPrev.stream()
                                .map(
                                        TotalCalculations::getSum
                                ).mapToDouble(a->a).average().orElse(prevCalc.getSum());
                        mean = mean > 0 ? mean : 0.0;

                        MeterReading meanMr = new MeterReading();
                        meanMr.setAddress(address);
                        meanMr.setUtility(utility);
                        meanMr.setDatetime(to);
                        meanMr.setValue(prevCalcMeterReading == null ? 0: prevCalcMeterReading.getValue() + (int)(mean / price));
                        meterReadingRepository.save(meanMr);

                        newCalculations.add(
                                totalCalculationsRepository.save(
                                        TotalCalculations
                                                .builder()
                                                .utility(utility)
                                                .calculationType(CalculationType.MEAN)
                                                .meterReading(meanMr)
                                                .datetime(LocalDateTime.now())
                                                .subscriberAddress(address)
                                                .sum(mean)
                                                .build())
                        );
                    } else {
                        double price = tariffUtilityRelationRepository.findFirstByTariffAndUtility(tariff, utility).getPrice();
                        newCalculations.add(
                                totalCalculationsRepository.save(
                                        TotalCalculations
                                                .builder()
                                                .utility(utility)
                                                .calculationType(CalculationType.METER_READING_BASED)
                                                .datetime(LocalDateTime.now())
                                                .meterReading(nowCalcMeterReading)
                                                .subscriberAddress(address)
                                                .sum((nowCalcMeterReading.getValue() - (prevCalcMeterReading == null ? 0.0: prevCalcMeterReading.getValue())) * price)
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
                                            .reduce(0.0, Double::sum)
                            )
                            .paid(false)
                            .subscriberAddress(address)
                            .dateTime(LocalDateTime.now())
                            .build()
            );
        }
    }
}

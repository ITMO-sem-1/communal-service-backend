package ru.itmo.communal.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.itmo.communal.entity.MeterReading;
import ru.itmo.communal.entity.SubscriberAddress;
import ru.itmo.communal.repository.MeterReadingRepository;
import ru.itmo.communal.repository.SubscriberAddressRepository;

import java.time.LocalDateTime;
import java.time.Month;
import java.time.Year;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TotalCalculationService {
    private final SubscriberAddressRepository subscriberAddressRepository;
    private final MeterReadingRepository meterReadingRepository;

    public void calculate() {
        LocalDateTime from, to;
        from = LocalDateTime.now().withDayOfMonth(1).withHour(0).withMinute(0);
        to = LocalDateTime.now().withDayOfMonth(Month.of(from.getMonthValue()).length(Year.isLeap(from.getYear()))).withHour(0).withMinute(0);
        List<SubscriberAddress> addresses = subscriberAddressRepository.findAll();
        for (SubscriberAddress address : addresses) {
            MeterReading meterReading = meterReadingRepository
                    .getLastMeterReadingForAddress(
                            address,
                            from,
                            to
                    );
            System.out.println();

            if (meterReading == null) {
                // в этом месяце чел не подавал показаний
            }

        }

    }

}

package ru.itmo.communal.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.itmo.communal.entity.MeterReading;
import ru.itmo.communal.entity.SubscriberAddress;

import java.time.LocalDateTime;
import java.util.List;

public interface MeterReadingRepository extends JpaRepository<MeterReading, Integer> {
//    List<MeterReading> findMeterReadingsByAddressAndDatetimeGreaterThanEqualAndDatetimeLessThanEqualOrderByDatetimeDesc(SubscriberAddress address, LocalDateTime from, LocalDateTime to);
    @Query(value = "select m from MeterReading m where m.address = ?1 and m.datetime = (select MAX(m1.datetime) from MeterReading m1 where m.address = m1.address and m1.datetime between ?2 and ?3)")
    MeterReading getLastMeterReadingForAddress(SubscriberAddress address, LocalDateTime from, LocalDateTime to);
}

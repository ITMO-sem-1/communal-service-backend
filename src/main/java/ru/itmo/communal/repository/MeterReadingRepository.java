package ru.itmo.communal.repository;

import org.springframework.boot.autoconfigure.amqp.RabbitConnectionDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.itmo.communal.entity.MeterReading;
import ru.itmo.communal.entity.PublicUtility;
import ru.itmo.communal.entity.SubscriberAddress;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface MeterReadingRepository extends JpaRepository<MeterReading, Integer> {
//    List<MeterReading> findMeterReadingsByAddressAndDatetimeGreaterThanEqualAndDatetimeLessThanEqualOrderByDatetimeDesc(SubscriberAddress address, LocalDateTime from, LocalDateTime to);
    @Query(value = "select m from MeterReading m where m.address = ?1 and m.datetime = (select MAX(m1.datetime) from MeterReading m1 where m.address = m1.address and m1.datetime between ?3 and ?4 and m1.utility=?2)")
    MeterReading getLastMeterReadingForAddress(SubscriberAddress address, PublicUtility utility, LocalDateTime from, LocalDateTime to);

    Optional<MeterReading> findFirstByAddressAndUtilityOrderByDatetimeDesc(SubscriberAddress address, PublicUtility utility);
    List<MeterReading> findAllByDatetimeGreaterThanEqualAndDatetimeLessThanAndAddress(LocalDateTime from, LocalDateTime to, SubscriberAddress subscriberAddress);
}

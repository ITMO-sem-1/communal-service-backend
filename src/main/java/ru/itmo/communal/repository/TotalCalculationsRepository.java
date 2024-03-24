package ru.itmo.communal.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.itmo.communal.entity.PublicUtility;
import ru.itmo.communal.entity.SubscriberAddress;
import ru.itmo.communal.entity.TotalCalculations;

import java.util.List;

public interface TotalCalculationsRepository extends JpaRepository<TotalCalculations, Integer> {
    TotalCalculations findFirstBySubscriberAddressAndUtilityOrderByDatetimeDesc(SubscriberAddress address, PublicUtility utility);
    List<TotalCalculations> findBySubscriberAddressAndUtilityOrderByDatetimeDesc(SubscriberAddress address, PublicUtility utility);
}

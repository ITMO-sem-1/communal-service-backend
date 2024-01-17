package ru.itmo.communal.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.itmo.communal.entity.PublicUtility;
import ru.itmo.communal.entity.SubscriberAddress;
import ru.itmo.communal.entity.TotalCalculations;

public interface TotalCalculationsRepository extends JpaRepository<TotalCalculations, Integer> {
    TotalCalculations findFirstBySubscriberAddressAndUtilityOrderByDatetimeDesc(SubscriberAddress address, PublicUtility utility);
}

package ru.itmo.communal.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.itmo.communal.entity.SubscriberAddress;

import java.util.Optional;

public interface SubscriberAddressRepository extends JpaRepository<SubscriberAddress, Integer> {

    Optional<SubscriberAddress> findById(int i);

    Optional<SubscriberAddress> getSubscriberAddressByAccountNumber(String accountNumber);
}

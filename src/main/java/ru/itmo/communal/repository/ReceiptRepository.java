package ru.itmo.communal.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.itmo.communal.entity.Receipt;
import ru.itmo.communal.entity.SubscriberAddress;

import java.util.List;

public interface ReceiptRepository extends JpaRepository<Receipt, Integer> {
    List<Receipt> findAllByPaidAndSubscriberAddress(boolean paid, SubscriberAddress subscriberAddress);
}
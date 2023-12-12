package ru.itmo.communal.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import ru.itmo.communal.entity.SubscriberAddress;
import ru.itmo.communal.repository.SubscriberAddressRepository;

import java.util.Optional;

@Component
public class DataInitializer implements CommandLineRunner {

    private final SubscriberAddressRepository subscriberAddressRepository;

    @Autowired
    public DataInitializer(SubscriberAddressRepository subscriberAddressRepository) {
        this.subscriberAddressRepository = subscriberAddressRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        Optional<SubscriberAddress> address = subscriberAddressRepository.getSubscriberAddressByAccountNumber("123456");
        if (address.isEmpty()) {
            SubscriberAddress subscriberAddress = new SubscriberAddress();
            subscriberAddress.setAccountNumber("123456");
            subscriberAddress.setGeoLocation("Some Location");
            subscriberAddressRepository.save(subscriberAddress);
        }

    }
}

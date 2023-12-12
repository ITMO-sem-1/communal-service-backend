package ru.itmo.communal.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Component;
import ru.itmo.communal.entity.PublicUtility;
import ru.itmo.communal.entity.SubscriberAddress;
import ru.itmo.communal.repository.PublicUtilityRepository;
import ru.itmo.communal.repository.SubscriberAddressRepository;

import java.util.HashMap;
import java.util.Optional;

@Component
public class DataInitializer implements CommandLineRunner {

    private final SubscriberAddressRepository subscriberAddressRepository;
    private final PublicUtilityRepository publicUtilityRepository;

    @Autowired
    public DataInitializer(SubscriberAddressRepository subscriberAddressRepository, PublicUtilityRepository publicUtilityRepository) {
        this.subscriberAddressRepository = subscriberAddressRepository;
        this.publicUtilityRepository = publicUtilityRepository;

    }

    @Override
    public void run(String... args) throws Exception {
        //Создание адреса
        Optional<SubscriberAddress> address = subscriberAddressRepository.getSubscriberAddressByAccountNumber("123456");
        if (address.isEmpty()) {
            SubscriberAddress subscriberAddress = new SubscriberAddress();
            subscriberAddress.setAccountNumber("123456");
            subscriberAddress.setGeoLocation("Some Location");
            subscriberAddressRepository.save(subscriberAddress);
        }

        // Создание ком. услуг
        var defaultPublicUtilities = new HashMap<String, String>();
        defaultPublicUtilities.put("Холодная вода", "чашки");
        defaultPublicUtilities.put("Горячая вода", "чашки");
        defaultPublicUtilities.put("Тепло", "Сожженые свечки");
        defaultPublicUtilities.put("Электричество", "молнии");

        for (var entry : defaultPublicUtilities.entrySet()) {
            PublicUtility utility = PublicUtility.builder().name(entry.getKey()).measure(entry.getValue()).build();
            if (!publicUtilityRepository.exists(Example.of(utility))) {
                publicUtilityRepository.save(utility);
            }
        }
    }
}


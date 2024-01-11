package ru.itmo.communal.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.itmo.communal.entity.Tariff;

public interface TariffRepository extends JpaRepository<Tariff, Integer> {
}

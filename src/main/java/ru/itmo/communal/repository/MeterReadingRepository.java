package ru.itmo.communal.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.itmo.communal.entity.MeterReading;

public interface MeterReadingRepository extends JpaRepository<MeterReading, Integer> {
}

package ru.itmo.communal.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.itmo.communal.entity.TariffUtilityRelation;

public interface TariffUtilityRepository extends JpaRepository<TariffUtilityRelation, Integer> {
}

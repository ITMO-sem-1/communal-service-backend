package ru.itmo.communal.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.itmo.communal.entity.PublicUtility;
import ru.itmo.communal.entity.Tariff;
import ru.itmo.communal.entity.TariffUtilityRelation;

public interface TariffUtilityRelationRepository extends JpaRepository<TariffUtilityRelation, Integer> {
    TariffUtilityRelation findFirstByTariffAndUtility(Tariff tariff, PublicUtility utility);
}

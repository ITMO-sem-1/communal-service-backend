package ru.itmo.communal.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.itmo.communal.entity.PublicUtility;

public interface PublicUtilityRepository extends JpaRepository<PublicUtility, Integer> {
}

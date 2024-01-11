package ru.itmo.communal.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tariff_utility")
public class TariffUtilityRelation {

    @Id
    @GeneratedValue
    private Integer id;
    @OneToOne
    private Tariff tariff;
    @OneToOne
    private PublicUtility utility;
}

package ru.itmo.communal.entity;

import jakarta.persistence.*;
import lombok.*;


@Entity
@Data
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
public class TotalCalculations {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private int sum;
    @Enumerated
    private CalculationType calculationType;


}

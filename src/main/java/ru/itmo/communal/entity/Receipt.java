package ru.itmo.communal.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Data
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
@Entity
public class Receipt {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private long sum;

    @OneToMany
    private List<TotalCalculations> utilityCalculations;

    private boolean paid;

    @ManyToOne
    private SubscriberAddress subscriberAddress;
}

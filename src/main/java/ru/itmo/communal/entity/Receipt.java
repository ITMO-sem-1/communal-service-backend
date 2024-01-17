package ru.itmo.communal.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
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
    @Transient
    private long credit;

    @OneToMany
    private List<TotalCalculations> utilityCalculations;

    private boolean paid;

    private LocalDateTime dateTime;

    @ManyToOne
    private SubscriberAddress subscriberAddress;
}

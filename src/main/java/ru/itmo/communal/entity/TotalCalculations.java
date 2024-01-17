package ru.itmo.communal.entity;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;


@Entity
@Data
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
public class TotalCalculations {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private Long sum;
    @Enumerated
    private CalculationType calculationType;

    private LocalDateTime datetime;

    @ManyToOne
    private PublicUtility utility;

//    @ManyToOne
//    private Receipt receipt;

    @ManyToOne
    private SubscriberAddress subscriberAddress;

    @OneToOne
    @Nullable
    private MeterReading meterReading;
}

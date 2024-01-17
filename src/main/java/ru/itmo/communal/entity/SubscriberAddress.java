package ru.itmo.communal.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="subscriber_address")
public class SubscriberAddress {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String accountNumber;
    private String geoLocation;
    @ManyToOne
    private Tariff tariff;

    @ManyToMany
    private List<PublicUtility> utilitiesEnabled;
}

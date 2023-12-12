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
@Table(name="subscriber_adress")
public class SubscriberAddress {

    @Id
    @GeneratedValue
    private Integer id;
    private String accountNumber;
    private String geoLocation;

}

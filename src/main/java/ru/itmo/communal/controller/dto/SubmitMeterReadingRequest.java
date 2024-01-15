package ru.itmo.communal.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SubmitMeterReadingRequest {
    private Integer utilityId;
    private Integer addressId;
    private Integer value;
}

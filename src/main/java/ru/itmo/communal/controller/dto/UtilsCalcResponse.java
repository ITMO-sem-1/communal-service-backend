package ru.itmo.communal.controller.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class UtilsCalcResponse {
    private String utilName;
    private Long utilPrice;
}

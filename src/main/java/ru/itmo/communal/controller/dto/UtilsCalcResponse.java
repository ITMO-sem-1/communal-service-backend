package ru.itmo.communal.controller.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UtilsCalcResponse {
    private String utilName;
    private String utilMeasure;
    private Long utilValue;
    private Long utilPrice;
}

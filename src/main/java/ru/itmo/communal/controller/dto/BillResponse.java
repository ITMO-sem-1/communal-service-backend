package ru.itmo.communal.controller.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;


@Data
@Builder
public class BillResponse {
    private Integer id;
    private Long credit;
    private Integer addrId;
    private LocalDateTime date;
    private List<UtilsCalcResponse> utils;
    private Boolean isPaid;
}

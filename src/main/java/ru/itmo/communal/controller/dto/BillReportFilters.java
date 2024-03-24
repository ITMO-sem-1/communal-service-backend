package ru.itmo.communal.controller.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BillReportFilters {
    LocalDateTime from;
    LocalDateTime to;
    Integer addressId;
}

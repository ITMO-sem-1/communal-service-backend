package ru.itmo.communal.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.itmo.communal.service.TotalCalculationService;

@RestController
@RequestMapping("/report")
@RequiredArgsConstructor
public class ReportController {

    @Autowired
    private TotalCalculationService totalCalculationService;
    @GetMapping
    public void generateReports() {
        totalCalculationService.calculate();
    }
}

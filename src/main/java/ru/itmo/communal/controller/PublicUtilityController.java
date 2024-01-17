package ru.itmo.communal.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.itmo.communal.service.UserService;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/v1/public-utility")
@RequiredArgsConstructor
public class PublicUtilityController {

    private final UserService utilityService;
    @GetMapping("/all")
    public ResponseEntity<?> getAllAddresses() {
        return ResponseEntity.ok().body(utilityService.getAllService());
    }

}

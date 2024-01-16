package ru.itmo.communal.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@CrossOrigin("*")
public class TestController {

    @GetMapping("/test")
    public String test() {
        return "Hello, world!";
    }
}

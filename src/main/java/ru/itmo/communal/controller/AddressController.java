package ru.itmo.communal.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.itmo.communal.controller.dto.LinkAddressRequest;
import ru.itmo.communal.service.UserService;

import java.security.Principal;

@RestController
@RequestMapping("/api/v1/addresses")
@RequiredArgsConstructor
public class AddressController {
    private final UserService userService;

    @PostMapping
    public ResponseEntity<?> linkAddress(@RequestBody LinkAddressRequest request, Principal connectedUser){
        try {
            userService.linkAddress(request, connectedUser);
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok().build();
    }
}

package ru.itmo.communal.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.itmo.communal.controller.dto.ChangePasswordRequest;
import ru.itmo.communal.service.UserService;

import java.security.Principal;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@CrossOrigin("*")
public class UserController {

    private final UserService service;

    @PatchMapping
    public ResponseEntity<?> changePassword(
            @RequestBody ChangePasswordRequest request,
            Principal connectedUser
    ) {
        service.changePassword(request, connectedUser);
        return ResponseEntity.ok().build();
    }
}

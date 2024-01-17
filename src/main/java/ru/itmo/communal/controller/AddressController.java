package ru.itmo.communal.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.itmo.communal.controller.dto.LinkAddressRequest;
import ru.itmo.communal.controller.dto.UtilityRequest;
import ru.itmo.communal.service.UserService;

import java.security.Principal;

@RestController
@RequestMapping("/api/v1/addresses")
@RequiredArgsConstructor
@CrossOrigin("*")
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

    @GetMapping
    public ResponseEntity<?> getAddress(Principal connectedUser){
        try {
            return ResponseEntity.ok().body(userService.getAddress(connectedUser));
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping
    public ResponseEntity<?> unlinkAddress(@RequestBody LinkAddressRequest request, Principal connectedUser){
        try {
            userService.unlinkAddress(request, connectedUser);
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{addressId}/services")
    public ResponseEntity<?> getServices( @PathVariable Integer addressId){
        try {
            return ResponseEntity.ok().body(userService.getServices(addressId));
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/{addressId}/services")
    public ResponseEntity<?> addServices(@PathVariable Integer addressId, @RequestBody UtilityRequest utilityRequest){
        try {
            return ResponseEntity.ok().body(userService.addService(addressId, utilityRequest));
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    @DeleteMapping("/{addressId}/services")
    public ResponseEntity<?> deleteServices(@PathVariable Integer addressId, @RequestBody UtilityRequest utilityRequest){
        try {
            return ResponseEntity.ok().body(userService.deleteService(addressId, utilityRequest));
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}

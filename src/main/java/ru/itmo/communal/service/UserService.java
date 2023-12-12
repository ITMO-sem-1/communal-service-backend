package ru.itmo.communal.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.itmo.communal.controller.dto.ChangePasswordRequest;
import ru.itmo.communal.controller.dto.LinkAddressRequest;
import ru.itmo.communal.entity.SubscriberAddress;
import ru.itmo.communal.entity.User;
import ru.itmo.communal.repository.SubscriberAddressRepository;
import ru.itmo.communal.repository.UserRepository;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository repository;
    private final SubscriberAddressRepository subscriberAddressRepository;
    public void changePassword(ChangePasswordRequest request, Principal connectedUser) {

        var user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();

        // check if the current password is correct
        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new IllegalStateException("Wrong password");
        }
        // check if the two new passwords are the same
        if (!request.getNewPassword().equals(request.getConfirmationPassword())) {
            throw new IllegalStateException("Password are not the same");
        }

        // update the password
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));

        // save the new password
        repository.save(user);
    }

    public void linkAddress(LinkAddressRequest linkAddressRequest, Principal connectedUser) {
        var user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();

        Optional<SubscriberAddress> address = subscriberAddressRepository.findById(linkAddressRequest.getSubscriberAddressId());
        if (address.isEmpty()) {
            throw new IllegalStateException("Попытка присвоить пользователю несуществующий адрес");
        }
        user.setAddresses(List.of(address.get()));
        repository.save(user);
    }
}

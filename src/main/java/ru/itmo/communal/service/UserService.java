package ru.itmo.communal.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.itmo.communal.controller.dto.ChangePasswordRequest;
import ru.itmo.communal.controller.dto.LinkAddressRequest;
import ru.itmo.communal.controller.dto.UtilityRequest;
import ru.itmo.communal.entity.PublicUtility;
import ru.itmo.communal.entity.SubscriberAddress;
import ru.itmo.communal.entity.User;
import ru.itmo.communal.repository.PublicUtilityRepository;
import ru.itmo.communal.repository.SubscriberAddressRepository;
import ru.itmo.communal.repository.UserRepository;

import java.security.Principal;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository repository;
    private final SubscriberAddressRepository subscriberAddressRepository;
    private final PublicUtilityRepository publicUtilityRepository;

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
        user.getAddresses().add(address.get());
        repository.save(user);
    }

    public List<SubscriberAddress> getAllAddress() {
        return subscriberAddressRepository.findAll();

    }

    public List<SubscriberAddress> getAddress(Principal connectedUser) {
        var user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();
        return repository.findById(user.getId()).map(User::getAddresses)
                .orElseThrow(() -> new IllegalStateException("Нет данного пользовтеля"));

    }

    public void unlinkAddress(LinkAddressRequest request, Principal connectedUser) {
        var user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();

        Optional<SubscriberAddress> address = subscriberAddressRepository.findById(request.getSubscriberAddressId());
        if (address.isEmpty()) {
            throw new IllegalStateException("Попытка присвоить пользователю несуществующий адрес");
        }
        user.setAddresses(user.getAddresses().stream().filter(subscriberAddress -> !Objects.equals(subscriberAddress.getId(), address.get().getId())).collect(Collectors.toList()));
        repository.save(user);
    }

    public List<PublicUtility> getServices(Integer addressId) {
        return subscriberAddressRepository.findById(addressId).map(SubscriberAddress::getUtilitiesEnabled)
                .orElseThrow(() ->new IllegalStateException("Несуществующий адрес"));
    }

    public List<PublicUtility> addService(Integer addressId, UtilityRequest utilityRequest) {
        SubscriberAddress subscriberAddress = subscriberAddressRepository.findById(addressId).orElseThrow(() -> new IllegalStateException("Несуществующий адрес"));
        subscriberAddress.getUtilitiesEnabled().add(publicUtilityRepository.findById(utilityRequest.getUtilityId()).get());
        subscriberAddressRepository.save(subscriberAddress);
        return subscriberAddress.getUtilitiesEnabled();
    }

    public List<PublicUtility> deleteService(Integer addressId, UtilityRequest utilityRequest) {
        SubscriberAddress subscriberAddress = subscriberAddressRepository.findById(addressId).orElseThrow(() -> new IllegalStateException("Несуществующий адрес"));
        subscriberAddress.getUtilitiesEnabled().remove(publicUtilityRepository.findById(utilityRequest.getUtilityId()).get());
        subscriberAddressRepository.save(subscriberAddress);
        return subscriberAddress.getUtilitiesEnabled();
    }
}

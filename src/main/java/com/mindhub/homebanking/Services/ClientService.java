package com.mindhub.homebanking.Services;

import com.mindhub.homebanking.models.Client;

import java.util.List;
import java.util.Optional;

public interface ClientService {
    List<Client> findAll();
    Optional<Client> findById(Long id);
    Client findByEmail(String email);
    Client findByUserName(String userName);
    Client findByPhoneNumber(String phoneNumber);
    void save(Client client);
}

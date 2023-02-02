package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.ClientDTO;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static java.util.stream.Collectors.toList;

@RequestMapping(path="/api")
@RestController
public class ClientController {
    @Autowired
    private ClientRepository clientRepository;
    @RequestMapping(path="/clients")
    public List<ClientDTO> getClients(){
        return clientRepository.findAll().stream().map(ClientDTO::new).collect(toList());
    }
    @RequestMapping(path="/clients/{id}")
    public ClientDTO getClient(@PathVariable Long id){
        //Esta deprecado pero no se de que otra forma hacerlo
//        return clientRepository.getById(id).map(ClientDTO::new).orElse(null);
        return clientRepository.findById(id).map(ClientDTO::new).orElse(null);
    }
}

package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.ClientDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

import static com.mindhub.homebanking.utils.Utilities.*;
import static java.util.stream.Collectors.toList;

@RequestMapping(path="/api")
@RestController
public class ClientController {
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
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
    @RequestMapping(path = "/clients", method = RequestMethod.POST)
    public ResponseEntity<Object> register(
            @RequestParam String firstName, @RequestParam String lastName,
            @RequestParam String email, @RequestParam String password,
            @RequestParam String userName, @RequestParam String phoneNumber) {

//        if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || password.isEmpty()) {
//            return new ResponseEntity<>("Missing data", HttpStatus.FORBIDDEN);
//        }
        if (firstName.isEmpty()) {
            return new ResponseEntity<>("Missing first name", HttpStatus.BAD_REQUEST);
        }
        if (lastName.isEmpty()) {
            return new ResponseEntity<>("Missing last name", HttpStatus.BAD_REQUEST);
        }
        if (email.isEmpty()) {
            return new ResponseEntity<>("Missing email", HttpStatus.BAD_REQUEST);
        }
        if (password.isEmpty()) {
            return new ResponseEntity<>("Missing password", HttpStatus.BAD_REQUEST);
        }
        if (userName.isEmpty()) {
            return new ResponseEntity<>("Missing username", HttpStatus.BAD_REQUEST);
        }
        if (phoneNumber.isEmpty()) {
            return new ResponseEntity<>("Missing phone number", HttpStatus.BAD_REQUEST);
        }
        if (!emailValidation(email)) {
            return new ResponseEntity<>("Invalid email format", HttpStatus.BAD_REQUEST);
        }
        if (clientRepository.findByEmail(email) !=  null) {
            return new ResponseEntity<>("Email already in use", HttpStatus.FORBIDDEN);
        }
        if (!userNameValidation(userName)) {
            return new ResponseEntity<>("Invalid username format", HttpStatus.BAD_REQUEST);
        }
        if (clientRepository.findByUserName(userName) !=  null) {
            return new ResponseEntity<>("Username already in use", HttpStatus.FORBIDDEN);
        }
        if (!phoneNumberValidation(phoneNumber)) {
            return new ResponseEntity<>("Invalid phone number format", HttpStatus.BAD_REQUEST);
        }
        if (clientRepository.findByPhoneNumber(phoneNumber) !=  null) {
            return new ResponseEntity<>("Phone number already in use", HttpStatus.FORBIDDEN);
        }
        Client client= new Client(firstName, lastName, email, passwordEncoder.encode(password),userName,phoneNumber);
        clientRepository.save(client);
        Account account=new Account("", LocalDateTime.now(),0);
        accountRepository.save(account);
        account.setNumber(accountNumberGenerator(account));
        client.addAccount(account);
        accountRepository.save(account);
        clientRepository.save(client);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
    @RequestMapping("/clients/current")
    public ClientDTO getCurrent(Authentication authentication) {
        System.out.println(authentication.getName());
        return new ClientDTO(clientRepository.findByEmail(authentication.getName()));
    }
}

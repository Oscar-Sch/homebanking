package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.Services.AccountService;
import com.mindhub.homebanking.Services.ClientService;
import com.mindhub.homebanking.dtos.ClientDTO;
import com.mindhub.homebanking.dtos.ClientFullDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.AccountType;
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
    private PasswordEncoder passwordEncoder;
    @Autowired
    private ClientService clientService;
    @Autowired
    private AccountService accountService;

    @GetMapping(path="/clients")
    public List<ClientFullDTO> getClients(){
        return clientService.findAll().stream().map(ClientFullDTO::new).collect(toList());
    }
    @GetMapping(path="/clients/{id}")
    public ClientFullDTO getClient(@PathVariable Long id){
        //Esta deprecado pero no se de que otra forma hacerlo
//        return clientRepository.getById(id).map(ClientDTO::new).orElse(null);
        return clientService.findById(id).map(ClientFullDTO::new).orElse(null);
    }
    @PostMapping(path = "/clients")
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
        if (clientService.findByEmail(email) !=  null) {
            return new ResponseEntity<>("Email already in use", HttpStatus.FORBIDDEN);
        }
        if (!userNameValidation(userName)) {
            return new ResponseEntity<>("Invalid username format", HttpStatus.BAD_REQUEST);
        }
        if (clientService.findByUserName(userName) !=  null) {
            return new ResponseEntity<>("Username already in use", HttpStatus.FORBIDDEN);
        }
        if (!phoneNumberValidation(phoneNumber)) {
            return new ResponseEntity<>("Invalid phone number format", HttpStatus.BAD_REQUEST);
        }
        if (clientService.findByPhoneNumber(phoneNumber) !=  null) {
            return new ResponseEntity<>("Phone number already in use", HttpStatus.FORBIDDEN);
        }
        Client client= new Client(firstName, lastName, email, passwordEncoder.encode(password),userName,phoneNumber);
        clientService.save(client);
        Account account=new Account(AccountType.SAVINGS,"", LocalDateTime.now(),0);
        accountService.save(account);
        account.setNumber(accountNumberGenerator(account));
        client.addAccount(account);
        accountService.save(account);
        clientService.save(client);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
    @GetMapping("/clients/current")
    public ClientDTO getCurrent(Authentication authentication) {
//        System.out.println(authentication.getName());
        return new ClientDTO(clientService.findByEmail(authentication.getName()));
    }
}

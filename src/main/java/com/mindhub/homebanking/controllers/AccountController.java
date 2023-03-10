package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.dtos.AccountShortDTO;
import com.mindhub.homebanking.dtos.ClientDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static com.mindhub.homebanking.utils.Utilities.accountNumberGenerator;
import static java.util.stream.Collectors.toList;

@RequestMapping(path="/api")
@RestController
public class AccountController {
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private ClientRepository clientRepository;
    @GetMapping(path="/accounts")
    public List<AccountDTO> getAccounts(){
        return accountRepository.findAll().stream().map(AccountDTO::new).collect(toList());
    }
    @GetMapping(path="/accounts/{id}")
    public AccountDTO getAccount(@PathVariable Long id){
        return accountRepository.findById(id).map(AccountDTO::new).orElse(null);
    }
    @GetMapping(path="/accounts/target")
    public AccountShortDTO getAccountbyNumber(@RequestParam String number){
        return accountRepository.findAccountByNumber(number).map(AccountShortDTO::new).orElse(null);
    }
    @PostMapping(path="/clients/current/accounts")
    public ResponseEntity<Object> createAccount(Authentication authentication) {

        Client client=clientRepository.findByEmail(authentication.getName());
        if(client.getAccounts().size()<3){
            Account account=new Account("", LocalDateTime.now(),0);
            accountRepository.save(account);
            account.setNumber(accountNumberGenerator(account));
            client.addAccount(account);
            accountRepository.save(account);
            clientRepository.save(client);
            return new ResponseEntity<>(HttpStatus.CREATED);
        }else{
            return new ResponseEntity<>("Can't create new accounts, you have already 3", HttpStatus.FORBIDDEN);
        }
    }
    @GetMapping(path="clients/current/accounts")
    public List<AccountDTO> getCurrentAccounts(Authentication authentication){
        return clientRepository.findByEmail(authentication.getName()).getAccounts().stream().map(AccountDTO::new).collect(toList());
    }
}

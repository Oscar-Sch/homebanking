package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.Services.AccountService;
import com.mindhub.homebanking.Services.ClientService;
import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.dtos.AccountShortDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.AccountType;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

import static com.mindhub.homebanking.utils.Utilities.accountNumberGenerator;
import static java.util.stream.Collectors.toList;

@RequestMapping(path="/api")
@RestController
public class AccountController {
    @Autowired
    private AccountService accountService;
    @Autowired
    private ClientService clientService;

    @GetMapping(path="/accounts")
    public List<AccountDTO> getAccounts(){
        return accountService.findAll().stream().map(AccountDTO::new).collect(toList());
    }
    @GetMapping(path="/accounts/{id}")
    public AccountDTO getAccount(@PathVariable Long id){
        return accountService.findById(id).map(AccountDTO::new).orElse(null);
    }
    @GetMapping(path="/accounts/target")
    public AccountShortDTO getAccountbyNumber(@RequestParam String number){
        return accountService.findAccountByNumber(number).map(AccountShortDTO::new).orElse(null);
    }
    @PostMapping(path="/clients/current/accounts")
    public ResponseEntity<Object> createAccount(@RequestParam AccountType type, Authentication authentication) {

        Client client=clientService.findByEmail(authentication.getName());
        if(client.getAccounts().stream().filter(Account::getActive).count()<3){
            Account account=new Account(type,"", LocalDateTime.now(),0);
            accountService.save(account);
            account.setNumber(accountNumberGenerator(account));
            client.addAccount(account);
            accountService.save(account);
            clientService.save(client);
            return new ResponseEntity<>(HttpStatus.CREATED);
        }else{
            return new ResponseEntity<>("Can't create new accounts, you have already 3", HttpStatus.FORBIDDEN);
        }
    }
    @GetMapping(path="clients/current/accounts")
    public List<AccountDTO> getCurrentAccounts(Authentication authentication){
        return clientService.findByEmail(authentication.getName()).getAccounts().stream().map(AccountDTO::new).collect(toList());
    }
    @DeleteMapping(path="clients/current/accounts")
    public ResponseEntity<Object> deleteAccount(@RequestParam String accountNumber,Authentication authentication){
        Account account= accountService.findByNumber(accountNumber);
        Client client= clientService.findByEmail(authentication.getName());

        if(account==null){
            return new ResponseEntity<>("This account doesn´t exists", HttpStatus.FORBIDDEN);
        }
        if(client.getAccounts().stream().noneMatch(acc-> acc.getNumber().equals(accountNumber))){
            return new ResponseEntity<>("This account doesn´t belongs to an authenticated client", HttpStatus.FORBIDDEN);
        }
        if (account.getBalance()>0){
            return new ResponseEntity<>("This account still has a positive balance, transfer the money to other account before deleting it", HttpStatus.FORBIDDEN);
        }
        account.setActive(false);
        accountService.save(account);
        return new ResponseEntity<>("Account deleted successfully", HttpStatus.OK);
    }
}

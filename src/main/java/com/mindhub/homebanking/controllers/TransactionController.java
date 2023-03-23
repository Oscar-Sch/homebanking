package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.Services.AccountService;
import com.mindhub.homebanking.Services.ClientService;
import com.mindhub.homebanking.Services.TransactionService;
import com.mindhub.homebanking.dtos.TransactionDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.Transaction;
import com.mindhub.homebanking.models.TransactionType;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RequestMapping(path="/api")
@RestController
public class TransactionController {

    @Autowired
    private AccountService accountService;
    @Autowired
    private TransactionService transactionService;
    @Autowired
    private ClientService clientService;

    @Transactional
    @PostMapping(path = "/transactions")
    public ResponseEntity<Object> makeTransaction(
            @RequestParam Double amount, @RequestParam String originAccountNumber,
            @RequestParam String targetAccountNumber,@RequestParam String description,
            Authentication authentication) {

        if (amount.isNaN()) {
            return new ResponseEntity<>("Missing transaction amount", HttpStatus.FORBIDDEN);
        }
        if (amount < 10.0) {
            return new ResponseEntity<>("Insufficient amount", HttpStatus.FORBIDDEN);
        }
        if (originAccountNumber.isEmpty()) {
            return new ResponseEntity<>("Missing origin account", HttpStatus.FORBIDDEN);
        }
        if (targetAccountNumber.isEmpty()) {
            return new ResponseEntity<>("Missing destination account", HttpStatus.FORBIDDEN);
        }
        if (targetAccountNumber.equals(originAccountNumber)) {
            return new ResponseEntity<>("Accounts shouldn't have the same number", HttpStatus.FORBIDDEN);
        }
        Account originAccount= accountService.findByNumber(originAccountNumber);
        Account targetAccount= accountService.findByNumber(targetAccountNumber);

        if (originAccount==null) {
            return new ResponseEntity<>("Origin account doesn't exists", HttpStatus.FORBIDDEN);
        }
        if (clientService.findByEmail(authentication.getName()).getAccounts().stream().noneMatch(acc-> acc.getNumber().equals(originAccountNumber))) {
            return new ResponseEntity<>("Origin account doesn´t belong to this user", HttpStatus.FORBIDDEN);
        }
        if (targetAccount==null) {
            return new ResponseEntity<>("Destination account doesn't exists", HttpStatus.FORBIDDEN);
        }
        if (originAccount.getBalance()<amount) {
            return new ResponseEntity<>("Insufficient funds", HttpStatus.FORBIDDEN);
        }
        Transaction debitTransaction=new Transaction(TransactionType.DEBIT,amount, LocalDateTime.now(),targetAccountNumber+": "+description,originAccountNumber,targetAccountNumber);
        Transaction creditTransaction=new Transaction(TransactionType.CREDIT,amount, LocalDateTime.now(),originAccountNumber+": "+description,targetAccountNumber,originAccountNumber);
        debitTransaction.setNewBalance(originAccount.getBalance()-amount);
        creditTransaction.setNewBalance(targetAccount.getBalance()+amount);
        originAccount.addTransaction(debitTransaction);
        targetAccount.addTransaction(creditTransaction);
        originAccount.setBalance(originAccount.getBalance()-amount);
        targetAccount.setBalance(targetAccount.getBalance()+amount);

        transactionService.save(debitTransaction);
        transactionService.save(creditTransaction);
        accountService.save(originAccount);
        accountService.save(targetAccount);
        return new ResponseEntity<>("Transaction done successfully", HttpStatus.CREATED);
    }
    @GetMapping(path="clients/current/transactions")
    public List<TransactionDTO> getCurrentTransactions(@RequestParam String accountNumber, @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fromDate, @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime thruDate, Authentication authentication){
        Client client= clientService.findByEmail(authentication.getName());
        Account account= accountService.findByNumber(accountNumber);

        if(account!=null && account.getActive() && client.getAccounts().stream().anyMatch(acc-> acc.getNumber().equals(accountNumber))) {
            Set<Transaction> transactions= account.getTransactions();
            if (fromDate != null && thruDate != null) {
                return transactions.stream().filter(transaction -> transaction.getDate().isAfter(fromDate) && transaction.getDate().isBefore(thruDate)).map(TransactionDTO::new).collect(Collectors.toList());
            }
            if(fromDate!=null){
                return transactions.stream().filter(transaction -> transaction.getDate().isAfter(fromDate)).map(TransactionDTO::new).collect(Collectors.toList());
            }
            if(thruDate!=null){
                return transactions.stream().filter(transaction -> transaction.getDate().isBefore(thruDate)).map(TransactionDTO::new).collect(Collectors.toList());
            }
            return transactions.stream().map(TransactionDTO::new).collect(Collectors.toList());
        }
        return null;
//        implementacion del metodo jpa que pedia la task
//        if(account!=null && account.getActive() && client.getAccounts().stream().anyMatch(acc-> acc.getNumber().equals(accountNumber))){
//            if(fromDate!=null && thruDate!=null){
//                return transactionRepository.findByDateBetween(fromDate,thruDate).stream().map(TransactionDTO::new).collect(Collectors.toList());
//            }
//        }else{
//            return transactionRepository.findAll().stream().map(TransactionDTO::new).collect(Collectors.toList());
//        }
//        return null;
    }
}

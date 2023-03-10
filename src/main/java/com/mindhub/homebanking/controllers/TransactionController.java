package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Transaction;
import com.mindhub.homebanking.models.TransactionType;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RequestMapping(path="/api")
@RestController
public class TransactionController {

    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private ClientRepository clientRepository;

    @Transactional
    @RequestMapping(path = "/transactions", method = RequestMethod.POST)
    public ResponseEntity<Object> makeTransaction(
            @RequestParam Double amount, @RequestParam String originAccountNumber,
            @RequestParam String targetAccountNumber,@RequestParam String description,
            Authentication authentication) {

        if (amount.isNaN()) {
            return new ResponseEntity<>("Missing transaction amount", HttpStatus.FORBIDDEN);
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
        Account originAccount= accountRepository.findByNumber(originAccountNumber);
        Account targetAccount= accountRepository.findByNumber(targetAccountNumber);

        if (originAccount==null) {
            return new ResponseEntity<>("Origin account doesn't exists", HttpStatus.FORBIDDEN);
        }
        if (clientRepository.findByEmail(authentication.getName()).getAccounts().stream().noneMatch(acc-> acc.getNumber().equals(originAccountNumber))) {
            return new ResponseEntity<>("Origin account doesn´t belong to this user", HttpStatus.FORBIDDEN);
        }
        if (targetAccount==null) {
            return new ResponseEntity<>("Destination account doesn't exists", HttpStatus.FORBIDDEN);
        }
        if (originAccount.getBalance()<amount) {
            return new ResponseEntity<>("Insufficient funds", HttpStatus.FORBIDDEN);
        }
        Transaction debitTransaction=new Transaction(TransactionType.DEBIT,amount, LocalDateTime.now(),targetAccountNumber+": "+description);
        Transaction creditTransaction=new Transaction(TransactionType.CREDIT,amount, LocalDateTime.now(),originAccountNumber+": "+description);
        originAccount.addTransaction(debitTransaction);
        targetAccount.addTransaction(creditTransaction);
        originAccount.setBalance(originAccount.getBalance()-amount);
        targetAccount.setBalance(targetAccount.getBalance()+amount);

        transactionRepository.save(debitTransaction);
        transactionRepository.save(creditTransaction);
        accountRepository.save(originAccount);
        accountRepository.save(targetAccount);
        return new ResponseEntity<>("Transaction done successfully", HttpStatus.CREATED);
    }
}

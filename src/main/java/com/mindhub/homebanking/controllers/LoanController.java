package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.LoanAplicationDTO;
import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientLoanRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.repositories.LoanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RequestMapping(path="/api")
@RestController
public class LoanController {

    @Autowired
    private LoanRepository loanRepository;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private ClientLoanRepository clientLoanRepository;
    @Transactional
    @PostMapping(path = "/loans")
    public ResponseEntity<Object> addLoan(@RequestBody LoanAplicationDTO loanAplicationDTO, Authentication authentication){
        Loan loan= loanRepository.findLoanById(loanAplicationDTO.getLoanID());
        Client client=clientRepository.findByEmail(authentication.getName());
        Account account= accountRepository.findByNumber(loanAplicationDTO.getAccountNumber());
        if (loan==null) {
            return new ResponseEntity<>("Incorrect loan id", HttpStatus.BAD_REQUEST);
        }
        if (loanAplicationDTO.getAmount() <= 0) {
            return new ResponseEntity<>("Insufficient amount", HttpStatus.BAD_REQUEST);
        }
        if (loanAplicationDTO.getPayments()<=0) {
            return new ResponseEntity<>("Incorrect loan id", HttpStatus.BAD_REQUEST);
        }
        if (loanAplicationDTO.getAmount()>loan.getMaxAmount()){
            return new ResponseEntity<>("Exeeded the loan maximum amount", HttpStatus.FORBIDDEN);
        }
        if (!loan.getPayments().contains(loanAplicationDTO.getPayments())){
            return new ResponseEntity<>("This loan doesn't have that payment option", HttpStatus.FORBIDDEN);
        }
        if (account==null){
            return new ResponseEntity<>("The account used to apply to the loan doesn't exists", HttpStatus.FORBIDDEN);
        }
        if (client.getAccounts().stream().noneMatch(acc -> acc.getNumber().equals(loanAplicationDTO.getAccountNumber()))){
            return new ResponseEntity<>("The account used to apply to the loan doesn't belongs to the client", HttpStatus.FORBIDDEN);
        }
        Double percentage=loan.getPercentages().get(loan.getPayments().indexOf(loanAplicationDTO.getPayments()));
        ClientLoan newLoan= new ClientLoan(loanAplicationDTO.getAmount()*(1+(percentage/100)), loanAplicationDTO.getPayments());
        Transaction transaction=new Transaction(TransactionType.CREDIT, loanAplicationDTO.getAmount(), LocalDateTime.now(),"Loan approved");
        client.addClientLoan(newLoan);
        loan.addClientLoan(newLoan);
        account.addTransaction(transaction);
        clientRepository.save(client);
        loanRepository.save(loan);
        accountRepository.save(account);
        clientLoanRepository.save(newLoan);
        return new ResponseEntity<>("New loan added to the account", HttpStatus.CREATED);
    }
}

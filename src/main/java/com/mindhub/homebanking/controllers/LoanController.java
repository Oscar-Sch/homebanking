package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.LoanAplicationDTO;
import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.*;
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
    @Autowired
    private TransactionRepository transactionRepository;
    @Transactional
    @PostMapping(path = "/loans")
    public ResponseEntity<Object> addLoan(@RequestBody LoanAplicationDTO loanAplicationDTO, Authentication authentication){
        Loan loan= loanRepository.findLoanById(loanAplicationDTO.getLoanId());
        Client client=clientRepository.findByEmail(authentication.getName());
        Account account= accountRepository.findByNumber(loanAplicationDTO.getAccountNumber());
        if (loan==null) {
            return new ResponseEntity<>("Incorrect loan id", HttpStatus.FORBIDDEN);
        }
        if (loanAplicationDTO.getAmount() <= 1000) {
            return new ResponseEntity<>("Insufficient amount", HttpStatus.FORBIDDEN);
        }
        if (loanAplicationDTO.getPayments()<=0) {
            return new ResponseEntity<>("Incorrect payments amount", HttpStatus.FORBIDDEN);
        }
        if (loanAplicationDTO.getAmount()>loan.getMaxAmount()){
            return new ResponseEntity<>("Exceeded the loan maximum amount", HttpStatus.FORBIDDEN);
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
        account.setBalance(account.getBalance()+transaction.getAmount());
        client.addClientLoan(newLoan);
        loan.addClientLoan(newLoan);
        account.addTransaction(transaction);
        clientLoanRepository.save(newLoan);
        transactionRepository.save(transaction);
        accountRepository.save(account);
        loanRepository.save(loan);
        clientRepository.save(client);
        return new ResponseEntity<>("New loan added to the account", HttpStatus.CREATED);
    }
}

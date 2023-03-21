package com.mindhub.homebanking.dtos;

import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.AccountType;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class AccountDTO {
    private long id;
    private String number;
    private LocalDateTime creationDate;
    private double balance;
    private AccountType type;
    private Boolean active;
    private List<TransactionDTO> transactions;
    private List<CoinDTO> coins;

    public AccountDTO() {
    }
    public AccountDTO(Account account) {
        this.id= account.getId();
        this.number= account.getNumber();
        this.creationDate=account.getCreationDate();
        this.balance= account.getBalance();
        this.transactions=account.getTransactions().stream().map(TransactionDTO::new).collect(Collectors.toList());
        this.active=account.getActive();
        this.type=account.getType();
        this.coins=account.getCoins().stream().map(CoinDTO::new).collect(Collectors.toList());
    }

    public long getId() {
        return id;
    }

    public String getNumber() {
        return number;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public double getBalance() {
        return balance;
    }

    public AccountType getType() {
        return type;
    }

    public Boolean getActive() {
        return active;
    }

    public List<CoinDTO> getCoins() {
        return coins;
    }

    public List<TransactionDTO> getTransactions() {
        return transactions;
    }

    @Override
    public String toString() {
        return "AccountDTO{" +
                "id=" + id +
                ", number='" + number + '\'' +
                ", creationDate=" + creationDate +
                ", balance=" + balance +
                '}';
    }
}

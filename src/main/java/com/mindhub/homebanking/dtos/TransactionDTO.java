package com.mindhub.homebanking.dtos;

import com.mindhub.homebanking.models.Transaction;
import com.mindhub.homebanking.models.TransactionType;

import java.time.LocalDateTime;
import java.util.List;

public class TransactionDTO {
    private long id;
    private TransactionType type;
    private LocalDateTime date;
    private double amount,newBalance;
    private String description,origin,destination;

    public TransactionDTO(){}

    public TransactionDTO(Transaction transaction){
        this.id=transaction.getId();
        this.type=transaction.getType();
        this.date=transaction.getDate();
        this.amount=transaction.getAmount();
        this.description=transaction.getDescription();
        this.origin= transaction.getOrigin();
        this.destination= transaction.getDestination();
        this.newBalance= transaction.getNewBalance();
    }

    public long getId() {
        return id;
    }

    public TransactionType getType() {
        return type;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public double getAmount() {
        return amount;
    }

    public String getDescription() {
        return description;
    }

    public String getOrigin() {
        return origin;
    }

    public String getDestination() {
        return destination;
    }

    public double getNewBalance() {
        return newBalance;
    }
}

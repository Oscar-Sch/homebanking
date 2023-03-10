package com.mindhub.homebanking.dtos;

import com.mindhub.homebanking.models.Account;

import java.time.LocalDateTime;

public class AccountShortDTO {
    private String number,owner;
    private LocalDateTime creationDate;

    public AccountShortDTO() {}

    public AccountShortDTO(Account account) {
        this.number = account.getNumber();
        this.owner = account.getClient().getFirstName()+" "+account.getClient().getLastName();
        this.creationDate = account.getCreationDate();
    }

    public String getNumber() {
        return number;
    }

    public String getOwner() {
        return owner;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }
}

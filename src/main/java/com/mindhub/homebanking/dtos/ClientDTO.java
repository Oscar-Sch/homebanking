package com.mindhub.homebanking.dtos;

import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Card;
import com.mindhub.homebanking.models.Client;

import java.util.List;
import java.util.stream.Collectors;

public class ClientDTO {
    private long id;
    private String firstName,lastName,email;
    private List<AccountDTO> accounts;
    private List<ClientLoanDTO> loans;
    private List<CardDTO> cards;
    public ClientDTO(){}
    public ClientDTO(Client client){
        this.id= client.getId();
        this.firstName= client.getFirstName();
        this.lastName= client.getLastName();
        this.email= client.getEmail();
        this.accounts=client.getAccounts().stream().filter(Account::getActive).map(AccountDTO::new).collect(Collectors.toList());
        this.loans=client.getClientLoans().stream().map(ClientLoanDTO::new).collect(Collectors.toList());
        this.cards=client.getCards().stream().filter(Card::getActive).map(CardDTO::new).collect(Collectors.toList());
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<AccountDTO> getAccounts() {
        return accounts;
    }

    public void setAccounts(List<AccountDTO> accounts) {
        this.accounts = accounts;
    }
    public List<ClientLoanDTO> getLoans() {
        return loans;
    }

    public void setLoans(List<ClientLoanDTO> loan) {
        this.loans = loan;
    }

    public List<CardDTO> getCards() {
        return cards;
    }

    @Override
    public String toString() {
        return "ClientDTO{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}

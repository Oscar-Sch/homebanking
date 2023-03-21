package com.mindhub.homebanking.models;

import com.mindhub.homebanking.repositories.CardRepository;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
public class Card {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long id;

    @Autowired
    @Transient
    private CardRepository cardRepository;
    private String cardHolder,number,cvv;
    private CardType type;
    private CardColor color;
    private LocalDate fromDate, thruDate;
    private Boolean active;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="client_id")
    private Client client;

    public Card() {}

    public Card(String cardHolder, CardType type, CardColor color) {
        this.cardHolder = cardHolder;
        this.type = type;
        this.color = color;
        this.fromDate=LocalDate.now();
        this.thruDate=LocalDate.now().plusYears(5);
        this.active =true;
    }

    public long getId() {
        return id;
    }

    public String getCardHolder() {
        return cardHolder;
    }

    public void setCardHolder(String cardHolder) {
        this.cardHolder = cardHolder;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getCvv() {
        return cvv;
    }

    public void setCvv(String cvv) {
        this.cvv = cvv;
    }

    public CardType getType() {
        return type;
    }

    public void setType(CardType type) {
        this.type = type;
    }

    public CardColor getColor() {
        return color;
    }

    public void setColor(CardColor color) {
        this.color = color;
    }

    public LocalDate getFromDate() {
        return fromDate;
    }

    public void setFromDate(LocalDate fromDate) {
        this.fromDate = fromDate;
    }

    public LocalDate getThruDate() {
        return thruDate;
    }

    public void setThruDate(LocalDate thruDate) {
        this.thruDate = thruDate;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    @Override
    public String toString() {
        return "Card{" +
                "id=" + id +
                ", cardHolder='" + cardHolder + '\'' +
                ", number='" + number + '\'' +
                ", cvv='" + cvv + '\'' +
                ", type=" + type +
                ", color=" + color +
                ", fromDate=" + fromDate +
                ", thruDate=" + thruDate +
                '}';
    }
}

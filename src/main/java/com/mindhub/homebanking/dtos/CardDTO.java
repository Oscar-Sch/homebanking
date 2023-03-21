package com.mindhub.homebanking.dtos;

import com.mindhub.homebanking.models.Card;
import com.mindhub.homebanking.models.CardColor;
import com.mindhub.homebanking.models.CardType;
import java.time.LocalDate;

public class CardDTO {
    private long id;

    private String cardHolder,number,cvv;
    private CardType type;
    private CardColor color;
    private LocalDate fromDate, thruDate;
    private Boolean active;

    public CardDTO() {}
    public CardDTO(Card card) {
        this.id= card.getId();
        this.cardHolder= card.getCardHolder();
        this.number=card.getNumber();
        this.cvv=card.getCvv();
        this.color=card.getColor();
        this.type=card.getType();
        this.fromDate=card.getFromDate();
        this.thruDate=card.getThruDate();
        this.active=card.getActive();
    }

    public long getId() {
        return id;
    }

    public String getCardHolder() {
        return cardHolder;
    }

    public String getNumber() {
        return number;
    }

    public String getCvv() {
        return cvv;
    }

    public CardType getType() {
        return type;
    }

    public CardColor getColor() {
        return color;
    }

    public LocalDate getFromDate() {
        return fromDate;
    }

    public LocalDate getThruDate() {
        return thruDate;
    }

    public Boolean getActive() {
        return active;
    }
}

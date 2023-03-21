package com.mindhub.homebanking.Services;

import com.mindhub.homebanking.models.Card;

public interface CardService {
    void save(Card card);
    Card findByNumber(String cardNumber);
    Boolean existsCardByNumber(String number);
}

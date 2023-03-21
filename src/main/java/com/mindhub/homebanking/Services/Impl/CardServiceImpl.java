package com.mindhub.homebanking.Services.Impl;

import com.mindhub.homebanking.Services.CardService;
import com.mindhub.homebanking.models.Card;
import com.mindhub.homebanking.repositories.CardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CardServiceImpl implements CardService {
    @Autowired
    private CardRepository cardRepository;
    @Override
    public void save(Card card) {
        cardRepository.save(card);
    }

    @Override
    public Card findByNumber(String cardNumber) {
        return cardRepository.findByNumber(cardNumber);
    }

    @Override
    public Boolean existsCardByNumber(String number) {
        return cardRepository.existsCardByNumber(number);
    }
}

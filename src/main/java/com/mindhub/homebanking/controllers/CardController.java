package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.dtos.CardDTO;
import com.mindhub.homebanking.models.Card;
import com.mindhub.homebanking.models.CardColor;
import com.mindhub.homebanking.models.CardType;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.CardRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import static com.mindhub.homebanking.utils.Utilities.cardCvvGenerator;
import static com.mindhub.homebanking.utils.Utilities.handleCardNumberGeneration;
import static java.util.stream.Collectors.toList;

@RequestMapping(path="/api")
@RestController
public class CardController {
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private CardRepository cardRepository;
    @RequestMapping(path="clients/current/cards", method= RequestMethod.POST)
    public ResponseEntity<Object> createCard(
            @RequestParam CardType cardType, @RequestParam CardColor cardColor, Authentication authentication){
        Client client=clientRepository.findByEmail(authentication.getName());
        if (!(cardType.equals(CardType.DEBIT)||cardType.equals(CardType.CREDIT)) || !(cardColor.equals(CardColor.TITANIUM) || cardColor.equals(CardColor.GOLD) || cardColor.equals(CardColor.SILVER))) {
            return new ResponseEntity<>("Incorrect data", HttpStatus.FORBIDDEN);
        }
        if(!(client.getCards().size()<6) ||
                !(client.getCards().stream().filter(card -> card.getType() == cardType).count()<3)){
            return new ResponseEntity<>("Credit cards limit reached", HttpStatus.FORBIDDEN);
        }
        if (client.getCards().stream().anyMatch(card -> (card.getType() == cardType && card.getColor() == cardColor) && card.getThruDate().isAfter(LocalDate.now()))){
            return new ResponseEntity<>("You already have a card of that type", HttpStatus.FORBIDDEN);
        }
        //si la tarjeta esta vencida... se updatea o se borra y crea una nueva???
        Card card=new Card(client.getFirstName()+" "+client.getLastName(),cardType,cardColor);
        card.setCvv(cardCvvGenerator());
        card.setNumber(handleCardNumberGeneration(cardRepository));
        client.addCard(card);
        cardRepository.save(card);
        clientRepository.save(client);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
    @RequestMapping(path="clients/current/cards")
    public List<CardDTO> getCurrentAccounts(Authentication authentication){
        return clientRepository.findByEmail(authentication.getName()).getCards().stream().map(CardDTO::new).collect(toList());
    }

}

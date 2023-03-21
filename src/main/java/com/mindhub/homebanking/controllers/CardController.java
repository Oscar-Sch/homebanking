package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.Services.AccountService;
import com.mindhub.homebanking.Services.CardService;
import com.mindhub.homebanking.Services.ClientService;
import com.mindhub.homebanking.Services.TransactionService;
import com.mindhub.homebanking.dtos.CardDTO;
import com.mindhub.homebanking.dtos.PostnetDTO;
import com.mindhub.homebanking.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static com.mindhub.homebanking.utils.Utilities.cardCvvGenerator;
import static com.mindhub.homebanking.utils.Utilities.handleCardNumberGeneration;
import static java.util.stream.Collectors.toList;

@RequestMapping(path="/api")
@RestController
public class CardController {
    @Autowired
    private AccountService accountService;
    @Autowired
    private ClientService clientService;
    @Autowired
    private TransactionService transactionService;
    @Autowired
    private CardService cardService;
    @PostMapping(path="clients/current/cards")
    public ResponseEntity<Object> createCard(
            @RequestParam CardType cardType, @RequestParam CardColor cardColor, Authentication authentication){
        Client client=clientService.findByEmail(authentication.getName());
        if (!(cardType.equals(CardType.DEBIT)||cardType.equals(CardType.CREDIT)) || !(cardColor.equals(CardColor.TITANIUM) || cardColor.equals(CardColor.GOLD) || cardColor.equals(CardColor.SILVER))) {
            return new ResponseEntity<>("Incorrect data", HttpStatus.FORBIDDEN);
        }
        if(!(client.getCards().stream().filter(Card::getActive).count()<6) ||
                !(client.getCards().stream().filter(card -> card.getType() == cardType && card.getActive()).count()<3)){
            return new ResponseEntity<>("Credit cards limit reached", HttpStatus.FORBIDDEN);
        }
        if (client.getCards().stream().anyMatch(card -> (card.getType() == cardType && card.getColor() == cardColor && card.getActive()) && card.getThruDate().isAfter(LocalDate.now()))){
            return new ResponseEntity<>("You already have a card of that type", HttpStatus.FORBIDDEN);
        }
        client.getCards().forEach(card -> {
            if((card.getType() == cardType && card.getColor() == cardColor && card.getActive()) && card.getThruDate().isBefore(LocalDate.now())){
                card.setActive(false);
                cardService.save(card);
            }
        });
        Card card=new Card(client.getFirstName()+" "+client.getLastName(),cardType,cardColor);
        card.setCvv(cardCvvGenerator());
        card.setNumber(handleCardNumberGeneration(cardService));
        client.addCard(card);
        cardService.save(card);
        clientService.save(client);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
    @GetMapping(path="clients/current/cards")
    public List<CardDTO> getCurrentAccounts(Authentication authentication){
        return clientService.findByEmail(authentication.getName()).getCards().stream().map(CardDTO::new).collect(toList());
    }
    @DeleteMapping(path="clients/current/cards")
    public ResponseEntity<Object> deleteCard(@RequestParam String cardNumber, Authentication authentication){
        Card card= cardService.findByNumber(cardNumber);
        Client client= clientService.findByEmail(authentication.getName());
        if(card==null){
            return new ResponseEntity<>("This card doesn´t exists", HttpStatus.FORBIDDEN);
        }
        if(client.getCards().stream().noneMatch(card1 -> card1.getNumber().equals(cardNumber))){
            return new ResponseEntity<>("This card doesn´t belongs to an authenticated client", HttpStatus.FORBIDDEN);
        }
        card.setActive(false);
        cardService.save(card);
        return new ResponseEntity<>("Card deleted successfully", HttpStatus.OK);
    }
    @PostMapping(path="cards/transaction")
    @CrossOrigin
    @Transactional
    public ResponseEntity<Object> cardTransaction(@RequestBody PostnetDTO postnetDTO){

        if(postnetDTO.getNumber().length()!=19){
            return new ResponseEntity<>("Invalid card number", HttpStatus.FORBIDDEN);
        }
        if (postnetDTO.getAmount() <= 0){
            return new ResponseEntity<>("Invalid amount", HttpStatus.FORBIDDEN);
        }
        if (postnetDTO.getCvv().isEmpty()){
            return new ResponseEntity<>("Invalid amount", HttpStatus.FORBIDDEN);
        }
        if (postnetDTO.getDescription().isEmpty()){
            return new ResponseEntity<>("Invalid description", HttpStatus.FORBIDDEN);
        }

        Card card=cardService.findByNumber(postnetDTO.getNumber());

        if (card == null){
            return new ResponseEntity<>("This card doesn't belongs to a client", HttpStatus.FORBIDDEN);
        }
        if (!card.getCvv().equals(postnetDTO.getCvv())){
            return new ResponseEntity<>("Incorrect cvv", HttpStatus.FORBIDDEN);
        }
        if (card.getThruDate().isBefore(LocalDate.now())){
            return new ResponseEntity<>("This card is due", HttpStatus.FORBIDDEN);
        }
        if(card.getType()!=CardType.DEBIT){
            return new ResponseEntity<>("This is not a debit card", HttpStatus.FORBIDDEN);
        }

        Account account= card.getClient().getAccounts().stream().filter(acc-> acc.getType()==AccountType.SAVINGS).collect(toList()).get(0);

        if(account.getBalance()<postnetDTO.getAmount()) {
            return new ResponseEntity<>("Insufficient money in your account", HttpStatus.FORBIDDEN);
        }
        Transaction transaction= new Transaction(TransactionType.DEBIT, postnetDTO.getAmount(), LocalDateTime.now(), postnetDTO.getDescription(),account.getNumber(),"Some store");
        account.addTransaction(transaction);
        account.setBalance(account.getBalance()- postnetDTO.getAmount());
        transaction.setNewBalance(account.getBalance());
        transactionService.save(transaction);
        accountService.save(account);
        return new ResponseEntity<>("Transaction done successfully", HttpStatus.OK);
    }
}

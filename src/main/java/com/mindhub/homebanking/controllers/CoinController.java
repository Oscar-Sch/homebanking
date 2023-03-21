package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.Services.AccountService;
import com.mindhub.homebanking.Services.ClientService;
import com.mindhub.homebanking.Services.CoinService;
import com.mindhub.homebanking.dtos.CoinTransferenceDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.AccountType;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.Coin;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.repositories.CoinRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RequestMapping(path="/api")
@RestController
public class CoinController {
    @Autowired
    private AccountService accountService;
    @Autowired
    private ClientService clientService;
    @Autowired
    private CoinService coinService;
    @Transactional
    @PostMapping(path="/clients/current/coins")
    public ResponseEntity<Object> transferCoin(@RequestBody List<CoinTransferenceDTO> transferenceList, Authentication authentication) {

        CoinTransferenceDTO origin= transferenceList.get(0);
        CoinTransferenceDTO destination= transferenceList.get(1);

        Client client=clientService.findByEmail(authentication.getName());
        Account originAccount=accountService.findByNumber(origin.getAccountNumber());
        Account destinationAccount=accountService.findByNumber(destination.getAccountNumber());
        if(origin.getAccountNumber().isEmpty()){
            return new ResponseEntity<>("Origin account number missing", HttpStatus.FORBIDDEN);
        }
        if(destination.getAccountNumber().isEmpty()){
            return new ResponseEntity<>("Destination account number missing", HttpStatus.FORBIDDEN);
        }
        if(originAccount==null){
            return new ResponseEntity<>("Origin account doesn't exists", HttpStatus.FORBIDDEN);
        }
        if(destinationAccount==null){
            return new ResponseEntity<>("Destination account doesn't exists", HttpStatus.FORBIDDEN);
        }
        if(destinationAccount.getType()!= AccountType.CRYPTO){
            return new ResponseEntity<>("Destination account doesn't allow cryptocurrencies", HttpStatus.FORBIDDEN);
        }
        if(client.getAccounts().stream().noneMatch(acc-> acc.getNumber().equals(origin.getAccountNumber()))){
            return new ResponseEntity<>("Origin account doesn't belongs to the authenticated client", HttpStatus.FORBIDDEN);
        }
        double coinsAmount;
        if (originAccount.getType()!=AccountType.CRYPTO){
            coinsAmount= origin.getAmount()/destination.getTransferRate();

            if(destinationAccount.getCoins().stream().anyMatch(coin->coin.getCoinId().equals(destination.getCoinId()))){
//            int index=account.getCoins().
                Coin coin = destinationAccount.getCoins().stream().filter(co->co.getCoinId().equals(destination.getCoinId())).collect(Collectors.toList()).get(0);
                //añadir transaccion
                coin.setAmount(coin.getAmount()+coinsAmount);
//                destinationAccount.addCoin(coin);
                originAccount.setBalance(originAccount.getBalance()- origin.getAmount());
                coinService.save(coin);
                accountService.save(originAccount);
                accountService.save(destinationAccount);
                return new ResponseEntity<>("Coin updated successfully", HttpStatus.CREATED);
            }
            Coin coin= new Coin(destination.getCoinId(), destination.getCoinName(), destination.getCoinLogo(), coinsAmount);
            destinationAccount.addCoin(coin);
            originAccount.setBalance(originAccount.getBalance()- origin.getAmount());
            coinService.save(coin);
            accountService.save(originAccount);
            accountService.save(destinationAccount);
            return new ResponseEntity<>("Coin created successfully", HttpStatus.CREATED);
        }else{
            coinsAmount= origin.getAmount()* origin.getTransferRate()/destination.getTransferRate();
            if(destinationAccount.getCoins().stream().anyMatch(coin->coin.getCoinId().equals(destination.getCoinId()))){

                Coin destinationCoin = destinationAccount.getCoins().stream().filter(co->co.getCoinId().equals(destination.getCoinId())).collect(Collectors.toList()).get(0);
                Coin originCoin = originAccount.getCoins().stream().filter(co->co.getCoinId().equals(origin.getCoinId())).collect(Collectors.toList()).get(0);
                //añadir transaccion
                destinationCoin.setAmount(destinationCoin.getAmount()+coinsAmount);
                originCoin.setAmount(originCoin.getAmount()- origin.getAmount());
//                destinationAccount.addCoin(coin);
                coinService.save(destinationCoin);
                coinService.save(originCoin);
                return new ResponseEntity<>("Coin updated successfully on transference", HttpStatus.CREATED);
            }
            Coin destinationCoin= new Coin(destination.getCoinId(), destination.getCoinName(), destination.getCoinLogo(), coinsAmount);
            Coin originCoin = originAccount.getCoins().stream().filter(co->co.getCoinId().equals(origin.getCoinId())).collect(Collectors.toList()).get(0);
            destinationAccount.addCoin(destinationCoin);
            originCoin.setAmount(originCoin.getAmount()- origin.getAmount());
            coinService.save(destinationCoin);
            coinService.save(originCoin);
            accountService.save(destinationAccount);
            return new ResponseEntity<>("Coin created successfully on transference", HttpStatus.CREATED);
        }
    }
//    @Transactional
//    @PostMapping(path="/clients/current/coins")
//    public ResponseEntity<Object> transferCoin(@RequestParam String accountNumber, @RequestParam String coinId,@RequestParam String coinName,@RequestParam String coinLogo, @RequestParam Double amount, Authentication authentication) {
//
//        Client client=clientRepository.findByEmail(authentication.getName());
//        Account account=accountRepository.findByNumber(accountNumber);
//        if(accountNumber.isEmpty()){
//            return new ResponseEntity<>("Account number missing", HttpStatus.FORBIDDEN);
//        }
//        if(account==null){
//            return new ResponseEntity<>("Account doesn't exists", HttpStatus.FORBIDDEN);
//        }
//        if(account.getType()!= AccountType.CRYPTO){
//            return new ResponseEntity<>("This account doesn't allow cryptocurrencies", HttpStatus.FORBIDDEN);
//        }
//        if(client.getAccounts().stream().noneMatch(acc-> acc.getNumber().equals(accountNumber))){
//            return new ResponseEntity<>("This account doesn't belongs to the authenticated client", HttpStatus.FORBIDDEN);
//        }
//        if(account.getCoins().stream().anyMatch(coin->coin.getCoinId().equals(coinId))){
////            int index=account.getCoins().
//            Coin coin = account.getCoins().stream().filter(co->co.getCoinId().equals(coinId)).collect(Collectors.toList()).get(0);
//            //añadir transaccion
//            coin.setAmount(coin.getAmount()+amount);
//            account.addCoin(coin);
//            coinRepository.save(coin);
//            accountRepository.save(account);
//            return new ResponseEntity<>("Coin updated successfully", HttpStatus.CREATED);
//        }
//        Coin coin= new Coin(coinId,coinName,coinLogo,amount);
//        account.addCoin(coin);
//        coinRepository.save(coin);
//        accountRepository.save(account);
//        return new ResponseEntity<>("Coin created successfully", HttpStatus.CREATED);
//    }
}

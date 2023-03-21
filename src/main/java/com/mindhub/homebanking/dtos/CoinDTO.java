package com.mindhub.homebanking.dtos;

import com.mindhub.homebanking.models.Coin;

public class CoinDTO {
    private long id;
    private String coinId,coinName,coinLogo;
    private Double amount;

    public CoinDTO(Coin coin){
        this.id=coin.getId();
        this.coinId= coin.getCoinId();
        this.coinName=coin.getCoinName();
        this.coinLogo=coin.getCoinLogo();
        this.amount= coin.getAmount();
    }

    public long getId() {
        return id;
    }

    public String getCoinId() {
        return coinId;
    }

    public String getCoinName() {
        return coinName;
    }

    public String getCoinLogo() {
        return coinLogo;
    }

    public Double getAmount() {
        return amount;
    }
}

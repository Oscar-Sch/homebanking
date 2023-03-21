package com.mindhub.homebanking.models;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
public class Coin {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long id;
    private String coinId,coinName,coinLogo;
    private Double amount;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="account_id")
    private Account account;
    public Coin(){}

    public Coin(String coinId, String coinName, String coinLogo, Double amount) {
        this.coinId = coinId;
        this.coinName = coinName;
        this.coinLogo = coinLogo;
        this.amount = amount;
    }

    public long getId() {
        return id;
    }
    public String getCoinId() {
        return coinId;
    }
    public void setCoinId(String coinId) {
        this.coinId = coinId;
    }
    public String getCoinName() {
        return coinName;
    }
    public void setCoinName(String coinName) {
        this.coinName = coinName;
    }
    public String getCoinLogo() {
        return coinLogo;
    }
    public void setCoinLogo(String coinLogo) {
        this.coinLogo = coinLogo;
    }
    public Double getAmount() {
        return amount;
    }
    public void setAmount(Double amount) {
        this.amount = amount;
    }
    public Account getAccount() {
        return account;
    }
    public void setAccount(Account account) {
        this.account = account;
    }
}

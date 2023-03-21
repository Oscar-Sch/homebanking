package com.mindhub.homebanking.dtos;



public class CoinTransferenceDTO {

    private String accountNumber;
    private String coinId;
    private String coinName;
    private String coinLogo;
    private Double amount;
    private Double transferRate;

    public CoinTransferenceDTO(String accountNumber,String coinId, String coinName, String coinLogo, Double amount, Double transferRate) {
        this.accountNumber=accountNumber;
        this.coinId = coinId;
        this.coinName = coinName;
        this.coinLogo = coinLogo;
        this.amount = amount;
        this.transferRate = transferRate;
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

    public Double getTransferRate() {
        return transferRate;
    }

    public String getAccountNumber() {
        return accountNumber;
    }
}

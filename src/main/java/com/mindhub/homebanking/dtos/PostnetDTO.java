package com.mindhub.homebanking.dtos;

public class PostnetDTO {
    private String number,cvv,description;
    private Double amount;

    public PostnetDTO(String number, String cvv, String description, Double amount) {
        this.number = number;
        this.cvv = cvv;
        this.description = description;
        this.amount = amount;
    }

    public String getNumber() {
        return number;
    }

    public String getCvv() {
        return cvv;
    }

    public String getDescription() {
        return description;
    }

    public Double getAmount() {
        return amount;
    }
}

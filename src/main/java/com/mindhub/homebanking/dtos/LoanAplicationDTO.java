package com.mindhub.homebanking.dtos;

public class LoanAplicationDTO {
    private Long loanID;
    private Double amount;
    private Integer payments;
    private String accountNumber;

    public LoanAplicationDTO() {}

    public Long getLoanID() {
        return loanID;
    }

    public Double getAmount() {
        return amount;
    }

    public Integer getPayments() {
        return payments;
    }

    public String getAccountNumber() {
        return accountNumber;
    }
}

package com.mindhub.homebanking.dtos;

public class LoanAplicationDTO {
    private Long loanId;
    private Double amount;
    private Integer payments;
    private String accountNumber;

    public LoanAplicationDTO() {}

    public LoanAplicationDTO(Long loanId, Double amount, Integer payments, String accountNumber) {
        this.loanId = loanId;
        this.amount = amount;
        this.payments = payments;
        this.accountNumber = accountNumber;
    }

    public Long getLoanId() {
        return loanId;
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

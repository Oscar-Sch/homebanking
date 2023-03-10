package com.mindhub.homebanking.models;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static java.util.stream.Collectors.toList;

@Entity
public class Loan {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native",strategy = "native")
    private long id;
    private String name;
    private double maxAmount;
    @ElementCollection
    private List<Integer> payments;
    @ElementCollection
    private List<Double> percentages;
    @OneToMany(mappedBy="loan", fetch=FetchType.EAGER)
    Set<ClientLoan> clientLoans= new HashSet<>();

    public Loan(){}

    public Loan(String name, double maxAmount, List<Integer> payments, List<Double> percentages){
        this.name=name;
        this.maxAmount= maxAmount;
        this.payments=payments;
        this.percentages=percentages;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getMaxAmount() {
        return maxAmount;
    }

    public void setMaxAmount(double maxAmount) {
        this.maxAmount = maxAmount;
    }

    public List<Integer> getPayments() {
        return payments;
    }

    public void setPayments(List<Integer> payments) {
        this.payments = payments;
    }
    public List<Client> getClients() {
        return clientLoans.stream().map(ClientLoan::getClient).collect(toList());
    }

    public Set<ClientLoan> getClientLoans() {
        return clientLoans;
    }

    public void setClientLoans(Set<ClientLoan> clientLoans) {
        this.clientLoans = clientLoans;
    }

    public List<Double> getPercentages() {
        return percentages;
    }

    public void setPercentages(List<Double> percentages) {
        this.percentages = percentages;
    }

    public void addClientLoan(ClientLoan clientLoan) {
        clientLoan.setLoan(this);
        this.clientLoans.add(clientLoan);
    }

}

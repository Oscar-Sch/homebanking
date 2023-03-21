package com.mindhub.homebanking.Services.Impl;

import com.mindhub.homebanking.Services.LoanService;
import com.mindhub.homebanking.models.Loan;
import com.mindhub.homebanking.repositories.LoanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class LoanServiceImpl implements LoanService {
    @Autowired
    private LoanRepository loanRepository;
    @Override
    public List<Loan> findAll() {
        return loanRepository.findAll();
    }

    @Override
    public Loan findLoanById(Long id) {
        return loanRepository.findLoanById(id);
    }

    @Override
    public void save(Loan loan) {
        loanRepository.save(loan);
    }
}

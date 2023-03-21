package com.mindhub.homebanking.Services;

import com.mindhub.homebanking.models.Transaction;

public interface TransactionService {
    void save(Transaction transaction);
}

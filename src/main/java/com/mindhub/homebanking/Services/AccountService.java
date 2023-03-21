package com.mindhub.homebanking.Services;

import com.mindhub.homebanking.models.Account;

import java.util.List;
import java.util.Optional;

public interface AccountService {
    List<Account> findAll();
    Optional<Account> findById(Long id);
    Optional<Account> findAccountByNumber(String number);
    Account findByNumber(String number);
    void save(Account account);
}

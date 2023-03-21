package com.mindhub.homebanking.Services.Impl;

import com.mindhub.homebanking.Services.CoinService;
import com.mindhub.homebanking.models.Coin;
import com.mindhub.homebanking.repositories.CoinRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CoinServiceImpl implements CoinService {
    @Autowired
    private CoinRepository coinRepository;
    @Override
    public void save(Coin coin) {
        coinRepository.save(coin);
    }
}

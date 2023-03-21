package com.mindhub.homebanking.repositories;

import com.mindhub.homebanking.models.Coin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface CoinRepository extends JpaRepository<Coin, Long> {
    Coin findByCoinId(String coinId);
}

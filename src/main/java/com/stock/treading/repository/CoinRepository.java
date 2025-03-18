package com.stock.treading.repository;

import com.stock.treading.modal.Coin;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CoinRepository extends JpaRepository<Coin, String> {

}

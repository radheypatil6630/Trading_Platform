package com.stock.treading.repository;


import com.stock.treading.modal.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WalletRepository extends JpaRepository<Wallet,Long> {

    Wallet findWalletByUserId(Long userId);
}

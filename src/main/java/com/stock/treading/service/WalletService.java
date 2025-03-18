package com.stock.treading.service;


import com.stock.treading.modal.Order;
import com.stock.treading.modal.User;
import com.stock.treading.modal.Wallet;



public interface WalletService {

    Wallet getUserWallet(User user );

    Wallet addBalance(Wallet wallet,Long amount);

    Wallet findWalletById(Long id) throws Exception;

    Wallet walletToWalletTransfer(User sender, Wallet receiverWallet,Long amount) throws Exception;

    Wallet payOrderPayment(Order order, User user) throws Exception;
}

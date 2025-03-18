package com.stock.treading.service;

import com.stock.treading.domain.OrderType;
import com.stock.treading.modal.Order;
import com.stock.treading.modal.User;
import com.stock.treading.modal.Wallet;
import com.stock.treading.repository.WalletRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Service
public class WalletServiceImpl implements WalletService{

    @Autowired
    private WalletRepository walletRepository;

    @Override
    public Wallet getUserWallet(User user) {
        Wallet wallet = walletRepository.findWalletByUserId(user.getId());
        if (wallet == null){
            wallet = new Wallet();
            wallet.setUser(user);
        }

        return wallet;
    }

    @Override
    public Wallet addBalance(Wallet wallet, Long amount) {
        BigDecimal balance = wallet.getBalance();
        BigDecimal newBalance = balance.add(BigDecimal.valueOf(amount));

        wallet.setBalance(newBalance);
        return walletRepository.save(wallet);
    }

    @Override
    public Wallet findWalletById(Long id) throws Exception {
        Optional<Wallet> wallet = walletRepository.findById(id);
        if (wallet.isPresent()){
            return  wallet.get();
        }
        throw  new Exception("Wallet Not found");
    }

    @Override
    public Wallet walletToWalletTransfer(User sender, Wallet receiverWallet, Long amount) throws Exception {
      Wallet senderWallet = getUserWallet(sender);
      if (senderWallet.getBalance().compareTo(BigDecimal.valueOf(amount))<0){
          throw  new Exception("Insufficient balance ...");
      }
      BigDecimal senderBalance = senderWallet.getBalance().subtract(BigDecimal.valueOf(amount));
       senderWallet.setBalance(senderBalance);
       walletRepository.save(senderWallet);

       BigDecimal receriverBalance = receiverWallet.getBalance().add(BigDecimal.valueOf(amount));
       receiverWallet.setBalance(receriverBalance);

       walletRepository.save(receiverWallet);

        return senderWallet;
    }

    @Override
    public Wallet payOrderPayment(Order order, User user) throws Exception {

        Wallet wallet = getUserWallet(user);

        if (order.getOrderType().equals(OrderType.BUY)){
            BigDecimal newBalance = wallet.getBalance().subtract(order.getPrice());

            if (newBalance.compareTo(order.getPrice())<0){
                throw new Exception("Insufficient funds for this trasaction");
            }

            wallet.setBalance(newBalance);

        }else{
            BigDecimal newBalance = wallet.getBalance().add(order.getPrice());
            wallet.setBalance(newBalance);
        }

        walletRepository.save(wallet);

        return wallet;
    }
}

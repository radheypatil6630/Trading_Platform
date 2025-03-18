package com.stock.treading.controller;

import com.stock.treading.modal.User;
import com.stock.treading.modal.Wallet;
import com.stock.treading.service.UserService;
import com.stock.treading.service.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/wallet")
public class WalletController {

    @Autowired
    private WalletService walletService;

    @Autowired
    private UserService userService;

    @GetMapping("/api/wallet")
    public ResponseEntity<?>  getUserWallet(@RequestHeader("Authorization")String jwt) throws Exception {
        User user = userService.findUserProfileByJwt(jwt);

        Wallet wallet = walletService.getUserWallet(user);

        return new ResponseEntity<>(wallet, HttpStatus.ACCEPTED);
    }


    @GetMapping("/api/wallet")
    public ResponseEntity<?>  walletToWalletTransfer(@RequestHeader("Authorization")String jwt, @PathVariable Long walletId,
                                                     @RequestBody Wallet) throws Exception {



        return null ;
    }
}

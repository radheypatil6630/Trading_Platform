package com.stock.treading.service;

import com.stock.treading.modal.TwoFactorOtp;
import com.stock.treading.modal.User;
import org.springframework.stereotype.Service;

@Service
public interface TwoFactorOtpService {

    TwoFactorOtp createTwoFactorOtp(User user, String otp, String jwt);

    TwoFactorOtp findByUser(Long userId);

    TwoFactorOtp findById(String id);

    boolean verifyTwoFactorOtp(TwoFactorOtp twoFactorOtp , String otp);

    void deleteTwoFactorOtp(TwoFactorOtp twoFactorOtp);
}

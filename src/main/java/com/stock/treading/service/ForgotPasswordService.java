package com.stock.treading.service;

import com.stock.treading.domain.VerificationType;
import com.stock.treading.modal.ForgotPasswordToken;
import com.stock.treading.modal.User;
import org.springframework.stereotype.Service;


public interface ForgotPasswordService {

    ForgotPasswordToken createToken(User user,
                                    String id,
                                    String otp,
                                    VerificationType verificationType
                                    ,String sendTo);

    ForgotPasswordToken findById(String id);

    ForgotPasswordToken findByUser(Long userId);

    void deleteToken(ForgotPasswordToken token);


}

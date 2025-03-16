package com.stock.treading.service;

import com.stock.treading.domain.VerificationType;
import com.stock.treading.modal.ForgotPasswordToken;
import com.stock.treading.modal.User;
import com.stock.treading.repository.ForgotPasswordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ForgotPasswordImpl implements  ForgotPasswordService{

    @Autowired
    private ForgotPasswordRepository forgotPasswordRepository;

    @Override
    public ForgotPasswordToken createToken(User user,
                                           String id,  String otp,
                                           VerificationType verificationType,
                                           String sendTo) {
       ForgotPasswordToken token = new ForgotPasswordToken();
       token.setUser(user);
       token.setSendTo(sendTo);
       token.setVerificationType(verificationType);
       token.setOtp(otp);
       token.setId(id);

        return forgotPasswordRepository.save(token);
    }

    @Override
    public ForgotPasswordToken findById(String id) {
        Optional<ForgotPasswordToken> token = forgotPasswordRepository.findById(id);

        return token.orElse(null);
    }

    @Override
    public ForgotPasswordToken findByUser(Long userId) {
        return forgotPasswordRepository.findByUserId(userId);
    }

    @Override
    public void deleteToken(ForgotPasswordToken token) {
        forgotPasswordRepository.delete(token);
    }
}

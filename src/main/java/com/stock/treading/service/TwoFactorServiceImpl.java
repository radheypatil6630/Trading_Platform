package com.stock.treading.service;

import com.stock.treading.modal.TwoFactorOtp;
import com.stock.treading.modal.User;
import com.stock.treading.repository.TwoFactorOtpRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class TwoFactorServiceImpl implements TwoFactorOtpService{

    @Autowired
    private TwoFactorOtpRepository twoFactorOtpRepository;


    @Override
    public TwoFactorOtp createTwoFactorOtp(User user, String otp, String jwt) {
        UUID uuid= UUID.randomUUID();
        String id = uuid.toString();

        TwoFactorOtp twoFactorOtp = new TwoFactorOtp();
        twoFactorOtp.setOtp(otp);
        twoFactorOtp.setJwt(jwt);
        twoFactorOtp.setId(id);
        twoFactorOtp.setUser(user);

        return  twoFactorOtpRepository.save( twoFactorOtp);
    }

    @Override
    public TwoFactorOtp findByUser(Long userId) {
        return twoFactorOtpRepository.findByUserId(userId);
    }

    @Override
    public TwoFactorOtp findById(String id) {
        Optional<TwoFactorOtp> optional = twoFactorOtpRepository.findById(id);
        return optional.orElse(null);
    }

    @Override
    public boolean verifyTwoFactorOtp(TwoFactorOtp twoFactorOtp, String otp) {

        return twoFactorOtp.getOtp().equals(otp);
    }

    @Override
    public void deleteTwoFactorOtp(TwoFactorOtp twoFactorOtp) {
        twoFactorOtpRepository.delete(twoFactorOtp);
    }
}

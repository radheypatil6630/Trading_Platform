package com.stock.treading.service;

import com.stock.treading.domain.VerificationType;
import com.stock.treading.modal.User;
import com.stock.treading.modal.VerificationCode;
import com.stock.treading.repository.VerificationCodeRepository;
import com.stock.treading.utils.OtpUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class VerificationCodeServiceImpl implements VerificationCodeService {

    @Autowired
    private VerificationCodeRepository verificationCodeRepository;

    @Override
    public VerificationCode sendVerificationCode(User user , VerificationType verificationType) {
        VerificationCode verificationCode1 = new VerificationCode();
        verificationCode1.setOtp(OtpUtils.generateOtp());
        verificationCode1.setVerificationType(verificationType);
        verificationCode1.setUser(user);


        return verificationCodeRepository.save(verificationCode1);
    }

    @Override
    public VerificationCode getVerificationCodeById(Long id) throws Exception {
        Optional<VerificationCode>  verificationCode = verificationCodeRepository.findById(id);

        if (verificationCode.isPresent()){
            return verificationCode.get();
        }
        throw  new Exception("verification code not found");

    }

    @Override
    public VerificationCode getVerificationCodeByUser(Long userId) {

        return verificationCodeRepository.findByUserId(userId);
    }

    @Override
    public void deleteVerificationCodeById(VerificationCode verificationCode) {
        verificationCodeRepository.delete(verificationCode);
    }
}

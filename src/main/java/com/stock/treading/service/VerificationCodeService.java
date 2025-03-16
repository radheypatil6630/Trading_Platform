package com.stock.treading.service;

import com.stock.treading.domain.VerificationType;
import com.stock.treading.modal.User;
import com.stock.treading.modal.VerificationCode;

import java.util.Optional;

public interface VerificationCodeService {

    VerificationCode sendVerificationCode(User user , VerificationType verificationType);

    VerificationCode getVerificationCodeById(Long id) throws Exception;

    VerificationCode getVerificationCodeByUser(Long userId);

    void deleteVerificationCodeById(VerificationCode verificationCode);



}

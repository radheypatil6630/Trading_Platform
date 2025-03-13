package com.stock.treading.repository;

import com.stock.treading.modal.TwoFactorOtp;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TwoFactorOtpRepository extends JpaRepository<TwoFactorOtp,String> {
    TwoFactorOtp findByUserId(Long userId);
}

package com.stock.treading.service;

import com.stock.treading.domain.VerificationType;
import com.stock.treading.modal.User;
import org.springframework.stereotype.Service;

@Service
public interface UserService {

    public User findUserProfileByJwt(String jwt) throws Exception;
    public User findUserByEmail(String email) throws Exception;
    public User findUserById(Long userId) throws Exception;

    public User enableTwoFactorAuthetication(VerificationType verificationType, String sendTo ,User user);

    User updatePassword(User user,String newPassword);

}

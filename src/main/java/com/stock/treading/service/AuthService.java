package com.stock.treading.service;

import com.stock.treading.modal.User;
import com.stock.treading.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    public User save(User user) throws Exception {
        if(user.getEmail()== null ||user.getMobileno().replaceAll("\\s","").isEmpty() || user.getMobileno() == null ||user.getEmail().replaceAll("\\s","").isEmpty() || user.getFullName() == null || user.getFullName().replaceAll("\\s","").isEmpty()||user.getPassword().replaceAll("\\s","").isEmpty()|| user.getPassword()== null){
            System.out.println("Please fill all the details");
        }

        User isEmailExist = userRepository.findByEmail(user.getEmail());

        if (isEmailExist != null){
            throw new Exception("email is alredy exist");
        }

        user.setEmail(user.getEmail().trim());
        user.setFullName(user.getFullName().trim());
        user.setPassword(user.getPassword().trim());

        return userRepository.save(user);

    }

//    private Authentication autheticate(User user){
////        return userRepository.findByEmail(user.getEmail());
//    }
}

package com.stock.treading.controller;


import com.stock.treading.request.ForgotPasswordTokenRequest;
import com.stock.treading.domain.VerificationType;
import com.stock.treading.modal.ForgotPasswordToken;
import com.stock.treading.modal.VerificationCode;
import com.stock.treading.request.ResetPasswordRequest;
import com.stock.treading.response.ApiResponse;
import com.stock.treading.response.AuthResponse;
import com.stock.treading.service.EmailService;
import com.stock.treading.service.ForgotPasswordService;
import com.stock.treading.service.UserService;
import  com.stock.treading.modal.User;
import com.stock.treading.service.VerificationCodeService;
import com.stock.treading.utils.OtpUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;


@RestController
public class UserController {

@Autowired
private UserService userService;

@Autowired
private EmailService emailService;

@Autowired
private VerificationCodeService verificationCodeService;

@Autowired
private ForgotPasswordService forgotPasswordService;

@GetMapping("/api/users/profile")
public ResponseEntity<User> getUserProfile(@RequestHeader("Authorization") String jwt) throws Exception {
    User user = userService.findUserProfileByJwt(jwt);
    return new ResponseEntity<>( user , HttpStatus.OK);
}

@PostMapping("/api/users/verification/{verificationType}/send-otp")
public ResponseEntity<String> sendVerificationOtp(@RequestHeader("Authorization") String jwt, @PathVariable VerificationType verificationType) throws Exception {

    User user = userService.findUserProfileByJwt(jwt);

    VerificationCode verificationCode = verificationCodeService.getVerificationCodeByUser(user.getId());

    if (verificationCode == null){
        verificationCode= verificationCodeService.sendVerificationCode(user,verificationType);
    }
    if(verificationType.equals(VerificationType.EMAIL)) {
        emailService.sendVerificationOtpEmail(user.getEmail(),verificationCode.getOtp());
    }


    return new ResponseEntity<>( "verification otp send succesfully " , HttpStatus.OK);
    }

    @PatchMapping("/api/users/enable-two-factor/verify-otp/{otp}")
    public ResponseEntity<User> enableTwoFactorAuthentication(
            @PathVariable String otp,
            @RequestHeader("Authorization") String jwt) throws Exception {
        User user = userService.findUserProfileByJwt(jwt);

        VerificationCode verificationCode = verificationCodeService.getVerificationCodeByUser(user.getId());
        String sendTo = verificationCode.getVerificationType().equals(VerificationType.EMAIL)?
                verificationCode.getEmail():verificationCode.getMobile();

        boolean isVerified = verificationCode.getOtp().equals(otp);

        if (isVerified){
            User updatedUser = userService.enableTwoFactorAuthetication(verificationCode.getVerificationType(),sendTo,user);

         verificationCodeService.deleteVerificationCodeById(verificationCode);

         return new ResponseEntity<>(updatedUser,HttpStatus.OK);
        }

        throw  new Exception("wrong otp");
    }

    @PostMapping("/auth/users/reset-password/send-otp")
    public ResponseEntity<AuthResponse> sendForgotPasswordOtp(
                                                        @RequestBody ForgotPasswordTokenRequest request) throws Exception {

        User user = userService.findUserByEmail(request.getSendTo());
        String otp = OtpUtils.generateOtp();
        UUID uuid = UUID.randomUUID();
        String id = uuid.toString();

        ForgotPasswordToken token =forgotPasswordService.findByUser(user.getId());

        if (token == null){
            token = forgotPasswordService.createToken(user,id,otp,request.getVerificationType(),request.getSendTo());
        }

        if (request.getVerificationType().equals(VerificationType.EMAIL)){
            emailService.sendVerificationOtpEmail(
                    user.getEmail()
                    ,token.getOtp());
        }
        AuthResponse response = new AuthResponse();
        response.setSession(token.getId());
        response.setMessage("Password reset otp sent successfully");

        return new ResponseEntity<>( response , HttpStatus.OK);
    }
    @PatchMapping("/auth/users/reset-password/verify-otp")
    public ResponseEntity<?> resetPassword(
            @RequestParam String id,
            @RequestBody ResetPasswordRequest request,
            @RequestHeader("Authorization") String jwt) throws Exception {
        User user = userService.findUserProfileByJwt(jwt);

        ForgotPasswordToken forgotPasswordToken = forgotPasswordService.findById(id);

        boolean isVerified = forgotPasswordToken.getOtp().equals(request.getOtp());

        if (isVerified){
            userService.updatePassword(forgotPasswordToken.getUser(),request.getPassword());
            ApiResponse response = new ApiResponse();
            response.setMessage("update password successfully");

            return new ResponseEntity<>(response,HttpStatus.ACCEPTED);
        }
        throw new Exception("wrong otp");
    }

}
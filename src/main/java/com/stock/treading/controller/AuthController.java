package com.stock.treading.controller;

import com.stock.treading.config.JwtProvider;
import com.stock.treading.modal.TwoFactorOtp;
import com.stock.treading.modal.User;
import com.stock.treading.repository.UserRepository;
import com.stock.treading.response.AuthResponse;
import com.stock.treading.service.AuthService;
import com.stock.treading.service.CustomUserDetailsService;
import com.stock.treading.service.EmailService;
import com.stock.treading.service.TwoFactorOtpService;
import com.stock.treading.utils.OtpUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@Slf4j
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthService authService;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Autowired
    private TwoFactorOtpService twoFactorOtpService;

    @Autowired
    private EmailService emailService;

    @PostMapping("/sign-up")
    public ResponseEntity<AuthResponse>  register(@RequestBody User user){

        try {
            User savedUser = authService.save(user);
            Authentication auth = new UsernamePasswordAuthenticationToken(
                    user.getEmail(),
                    user.getPassword()

            );
            SecurityContextHolder.getContext().setAuthentication(auth);

            String jwt = JwtProvider.generateToken(auth);

            AuthResponse res= new AuthResponse();
            res.setJwt(jwt);
            res.setStatus(true);
            res.setMessage("successfully registered");

            return new ResponseEntity<>(res, HttpStatus.CREATED);
        }catch (Exception e){

//            log.error(String.valueOf(e));
            return  new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }


    }

    @PostMapping("/sign-in")
    public ResponseEntity<AuthResponse> login(@RequestBody User user){

        try {
//            User savedUser = authService.save(user);

            String username = user.getEmail();
            String password = user.getPassword();

            Authentication auth = autheticate(username, password);
            SecurityContextHolder.getContext().setAuthentication(auth);

            String jwt = JwtProvider.generateToken(auth);

            User authUser = userRepository.findByEmail(username);

            if (user.getTwoFactorAuth().isEnabled()){
                AuthResponse res = new AuthResponse();
                res.setMessage("Two factor auth is enabled");
                res.setTwoFactorAuthEnabled(true);
                String otp = OtpUtils.generateOtp();

                TwoFactorOtp oldTwoFactorOtp = twoFactorOtpService.findByUser(authUser.getId());
                if (oldTwoFactorOtp != null){
                    twoFactorOtpService.deleteTwoFactorOtp(oldTwoFactorOtp);
                }
                TwoFactorOtp newTwoFactorOtp = twoFactorOtpService.createTwoFactorOtp(authUser,otp,jwt);

                emailService.sendVerificationOtpEmail(username,otp);

                res.setSession(newTwoFactorOtp.getId());
                return new ResponseEntity<>(res,HttpStatus.ACCEPTED);
            }

            AuthResponse res= new AuthResponse();
            res.setJwt(jwt);
            res.setStatus(true);
            res.setMessage("successfully login");

            return new ResponseEntity<>(res, HttpStatus.FOUND);
        }catch (Exception e){

            return  new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }


    }

    private Authentication autheticate(String username, String password) {
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);

        if (userDetails == null){
            throw new BadCredentialsException("Inavlid username");
        }

        if(!password.equals(userDetails.getPassword())){
            throw new BadCredentialsException("Invalid password");
        }

        return new UsernamePasswordAuthenticationToken(userDetails,password,userDetails.getAuthorities());
    }


    public  ResponseEntity<AuthResponse> verifySigninOtp(@PathVariable String otp,@RequestParam String id) throws Exception {

        TwoFactorOtp twoFactorOtp = twoFactorOtpService.findById(id);

        if (twoFactorOtpService.verifyTwoFactorOtp(twoFactorOtp,otp)){
            AuthResponse res = new AuthResponse();
            res.setMessage("Two factor authetication verified");
            res.setTwoFactorAuthEnabled(true);
            res.setJwt(twoFactorOtp.getJwt());

            return new ResponseEntity<>(res,HttpStatus.OK);
        }
        throw  new Exception("invalid otp");

    }

}

package com.stock.treading.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import javax.crypto.SecretKey;
import java.util.*;

public class JwtProvider {

    private static SecretKey key = Keys.hmacShaKeyFor(JwtConstant.SECRETE_KEY.getBytes());

    public static String generateToken(Authentication auth){

        Collection<? extends GrantedAuthority> authorites = auth.getAuthorities();
        String roles = populateAuthorities(authorites);

        String jwt = Jwts.builder()
                .setIssuedAt(new Date())
                .setExpiration(new Date(new Date().getTime()+86400000))
                .claim("email",auth.getName())
                .claim("authorities",roles)
                .signWith(key)
                .compact();
        return jwt;
    }

    public  static String getEmailFromJwtToken(String token){
        token = token.substring(7);
        Claims claims= Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();

        String email = String.valueOf(claims.get("email"));
        return  email;
    }

    private static String populateAuthorities(Collection<? extends GrantedAuthority> authorites){

        Set<String> auth = new HashSet<>();
        for (GrantedAuthority g: authorites){
            auth.add(g.getAuthority());


        }
        return String.join(",",auth);
    }
}

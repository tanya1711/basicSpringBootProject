package com.example.demo;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.openqa.selenium.Keys;
import org.springframework.boot.autoconfigure.ssl.SslBundleProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class JwtUtil {

    public String generateToken(String username){
        Map<String,Object> claims = new HashMap<>();
        return createToken(claims,username);
    }

    private String createToken(Map<String,Object> claims, String subject){
        byte[] secret = "Tanya".getBytes();

        return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000*60*60))
                .signWith(SignatureAlgorithm.HS256, secret).compact();
    }

//    public Key getKey(){
//        return Keys.hm
//    }

//    public Boolean validateToken(String token , UserDetails userDetails){
//            final String userName = extractUsername(token);
//            return (userName.equals(userDetails.getUsername()) && !isTokenExpired(token))
//    }
}

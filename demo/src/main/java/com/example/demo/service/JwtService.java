package com.example.demo.service;

import com.google.common.primitives.Bytes;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.openqa.selenium.By;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import static com.example.demo.SpringStarter.newtIME;

@Component
public class JwtService {

    public String generateToken(String username) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, username);
    }

    public String createToken(Map<String, Object> claims, String username) {

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(new Date(newtIME))
                .signWith(getKey(), SignatureAlgorithm.HS256)
                .compact();

    }

    public String extractUsername(String token){
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpirationData(String token){
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimResolver){

        final Claims claims = extractAllClaims(token);
        return claimResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {

        return Jwts
                .parserBuilder()
                .setSigningKey(getKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public Key getKey() {
        byte[] keyBytes = Decoders.BASE64.decode("E75204654FD608D49881A1A3B1F759D64729C705656BE7C82CF9495F45A09A8750C7CF16D54B3EE3F4766BDB9614075DC9B46780BC9FA2A88DAF4E6A16AFAC50138D12A4D9EC40E1B41C73AC7DEDF6E76576436FCB2B8BE3BDAF3BA8F349172CA0B1FF81D733BEE6404371316CB47810A51EE1864F2DCBC6D3E8FCA68A59DA72");
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
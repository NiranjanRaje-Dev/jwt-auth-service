package com.urbanest.auth.Utility;

import com.urbanest.auth.Entities.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtUtill {

    @Value("${jwt.secret.key")
    private static String secretKey;

    private static SecretKey getSecretKey(){
        return Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
    }

    public String generateAccessToken(User user){
        return Jwts.builder()
                .subject(user.getUsername())
                .claim("userId",user.getId().toString())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 10))
                .signWith(getSecretKey())
                .compact();
    }

    public static String getUserName(String bearerToken) {
        Claims body = Jwts.parser()
                .setSigningKey(getSecretKey())
                .build()
                .parseClaimsJwt(bearerToken)
                .getBody();

        return body.getSubject();
    }

    public static Boolean isTokenExpired(String token){
        Claims body = Jwts.parser()
                .setSigningKey(getSecretKey())
                .build()
                .parseClaimsJwt(token)
                .getBody();

        return body.getExpiration().before(new Date());
    }
}

package com.securitish.safebox.Service;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Base64;
import java.util.Date;

import static com.securitish.safebox.ApplicationConstants.AUTHORIZATION_HEADER;
import static com.securitish.safebox.ApplicationConstants.AUTHORIZATION_PREFIX;

@Service
public class TokenServiceImpl implements TokenService{
    static String secret = "ddddd";

    @Override
    public String generateToken(String username) {
        String token = Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 180000))
                .signWith(SignatureAlgorithm.HS256,secret.getBytes(StandardCharsets.UTF_8))
                .compact();
        return token;
    }

    @Override
    public Claims validateToken(HttpServletRequest request) {
        String jwtToken = request.getHeader(AUTHORIZATION_HEADER).replace(AUTHORIZATION_PREFIX, "");

        Claims claims = null;
        try {
            claims = Jwts.parser()
                    .setSigningKey(secret.getBytes())
                    .parseClaimsJws(jwtToken)
                    .getBody();
        } catch (Exception e) {
            System.out.println("Exception: " + e);
        }
        return claims;
    }
}

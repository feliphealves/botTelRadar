package com.botTelRadar.util;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.*;

@Component
public class JwtTokenUtil {

	@Value("${JWT_SECRET_KEY}")
	private String secretKey;

	@Value("${jwt.expiration}")
	private long expirationTime;
	
    public String generateToken(String username) {
    	System.out.println("Secret Key: " + secretKey);
    	
    	if (secretKey == null || secretKey.isEmpty()) {
            throw new IllegalArgumentException("A chave secreta não pode estar vazia");
        }
    	
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(SignatureAlgorithm.HS512, secretKey)
                .compact();
    }

    public String getUsernameFromToken(String token) {
        try {
            return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
        } catch (ExpiredJwtException e) {
            System.out.println("Token expirado");
            return null;
        } catch (JwtException | IllegalArgumentException e) {
            System.out.println("Token inválido");
            return null;
        }
    }

    public boolean isTokenExpired(String token) {
        return getExpirationDateFromToken(token).before(new Date());
    }

    public Date getExpirationDateFromToken(String token) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getExpiration();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
            return true; // O token é válido
        } catch (JwtException | IllegalArgumentException e) {
            return false; // O token é inválido
        }
    }
}

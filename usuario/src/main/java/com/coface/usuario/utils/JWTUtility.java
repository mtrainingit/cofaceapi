package com.coface.usuario.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Map;

@Component
public class JWTUtility {

    @Value("${jwt.key}")
    private String KEY;

    public String expedirToken(String id, String subject, Map<String, Object> claims) {
        return Jwts
                .builder()
                .claims(claims)
                .subject(subject)
                .id(id)
                .issuer("coface")
                .issuedAt(Date.from(Instant.now()))
                .expiration(Date.from(Instant.now().plus(15, ChronoUnit.DAYS)))
                .signWith(getKey(), Jwts.SIG.HS256)
                .compact();
    }

    private SecretKey getKey() {
        return Keys.hmacShaKeyFor(KEY.getBytes());
    }

    public boolean validarToken(String token) {
        return getPayload(token)
                .getExpiration()
                .after(Date.from(Instant.now()));
    }

    public String getId(String token) {
        return getPayload(token).getId();
    }

    public String getSubject(String token) {
        return getPayload(token).getSubject();
    }

    public Object getClaim(String token, String claim) {
        return getPayload(token).get(claim);
    }

    private Claims getPayload(String token) {
        return Jwts
                .parser()
                .verifyWith(getKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}

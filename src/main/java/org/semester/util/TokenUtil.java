package org.semester.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Encoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.Duration;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Component
public class TokenUtil {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.lifetime}")
    private Duration accessTokenLifetime;

    @Value("${jwt.refresh-lifetime}")
    private Duration refreshTokenLifetime;

    private SecretKey generateKey() {
        byte[] base64KeyBytes = Encoders.BASE64.encode(secret.getBytes()).getBytes();
        return Keys.hmacShaKeyFor(base64KeyBytes);
    }

    public Map<String, String> generatePair(String email, String role) {
        Map<String, String> output = new HashMap<>();
        output.put("access", generateToken(email, "access", role));
        output.put("refresh", generateToken(email, "refresh", role));
        return output;
    }

    public String generateToken(String email, String type, String role) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("email", email);
        claims.put("type", type);
        claims.put("role", role);
        Date issuedAt = new Date();
        Date expires;
        if (type.equals("access")) {
            expires = new Date(issuedAt.getTime() + accessTokenLifetime.toMillis());
        }
        else {
            expires = new Date(issuedAt.getTime() + refreshTokenLifetime.toMillis());
        }
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(issuedAt)
                .setExpiration(expires)
                .signWith(generateKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public Claims getClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(generateKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public String getEmail(String token) {
        return (String) getClaims(token).get("email");
    }

    public String getRole(String token) {
        return (String) getClaims(token).get("role");
    }
}

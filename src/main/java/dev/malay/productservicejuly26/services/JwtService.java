package dev.malay.productservicejuly26.services;

import dev.malay.productservicejuly26.models.User;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Service
public class JwtService {
    private final String secret;
    private final long expirationMs;

    public JwtService(@Value("${jwt.secret:}") String secret,
                      @Value("${jwt.expiration-ms:3600000}") long expirationMs) {
        this.secret = secret;
        this.expirationMs = expirationMs;
    }

    public String issue(User user) {
        Date now = new Date();
        return Jwts.builder().subject(user.getUsername()).claim("role", user.getRole().name())
                .issuedAt(now).expiration(new Date(now.getTime() + expirationMs))
                .signWith(key()).compact();
    }

    public String username(String token) {
        return Jwts.parser().verifyWith(key()).build().parseSignedClaims(token)
                .getPayload().getSubject();
    }

    public long expirationMs() { return expirationMs; }

    private SecretKey key() {
        if (secret == null || secret.getBytes(StandardCharsets.UTF_8).length < 32) {
            throw new IllegalStateException("JWT_SECRET must be configured with at least 32 bytes");
        }
        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }
}

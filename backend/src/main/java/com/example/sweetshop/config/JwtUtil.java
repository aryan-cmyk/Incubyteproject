package com.example.sweetshop.config;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.UUID;
import javax.crypto.SecretKey;

@Component
public class JwtUtil{
  private final SecretKey key;
  private final long expirationMs;

  public JwtUtil(@Value("${jwt.secret}") String secret,
                 @Value("${jwt.expiration-ms}") long expirationMs) {
    this.key = Keys.hmacShaKeyFor(secret.getBytes());
    this.expirationMs = expirationMs;
  }

  public String generateToken(String userId, String email, String role) {
    Date now = new Date();
    Date exp = new Date(now.getTime() + expirationMs);
    return Jwts.builder()
      .setSubject(userId)
      .claim("email", email)
      .claim("role", role)
      .setIssuedAt(now)
      .setExpiration(exp)
      .signWith(key)
      .compact();
  }

  public Jws<Claims> validateToken(String token) {
    return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
  }
}

package com.example.docease.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.expirationMs}")
    private String jwtExpirationMsString;

    @Value("${jwt.refreshExpirationMs}") // 🔥 Added refresh token expiry
    private String refreshTokenExpirationMsString;

    @Value("${jwt.passwordResetExpirationMs}") // 🔥 Added password reset token expiry
    private String passwordResetExpirationMsString;

    private long jwtExpirationMs;
    private long refreshTokenExpirationMs;
    private long passwordResetExpirationMs;

    private SecretKey key;

    @PostConstruct
    public void init() {
        try{
            System.out.println("Initializing JwtUtil...");

            key = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));

            System.out.println("Secret Key Initialized Successfully.");
            this.jwtExpirationMs = Long.parseLong(jwtExpirationMsString.trim());
            this.refreshTokenExpirationMs = Long.parseLong(refreshTokenExpirationMsString.trim());
            this.passwordResetExpirationMs = Long.parseLong(passwordResetExpirationMsString.trim());
            System.out.println("Token expiration times set successfully.");

        } catch(Exception e){
            System.err.println("Error initializing JwtUtil: " + e.getMessage());
            throw new RuntimeException("JwtUtil initialization failed.", e);
        }
    }

    // 🔹 Generate Access Token
    public String generateToken(String username, String role) {
        return Jwts.builder()
                .setSubject(username)
                .claim("role", role)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
                .signWith(key)
                .compact();
    }

    // 🔹 Generate Refresh Token
    public String generateRefreshToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + refreshTokenExpirationMs))
                .signWith(key)
                .compact();
    }

    // 🔹 Generate Password Reset Token
    public String generatePasswordResetToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + passwordResetExpirationMs))
                .signWith(key)
                .compact();
    }

    // 🔹 Extract Claims from Any Token
    public Claims extractClaims(String token) {
        return Jwts.parser()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public String extractUsername(String token) {
        return extractClaims(token).getSubject(); // Username is stored in the subject field
    }

    // 🔹 Validate Token Expiration & Integrity
    public boolean validateToken(String token) {
        try {
            extractClaims(token); // 🔥 Will throw if token is expired or tampered
            return true;
        } catch (ExpiredJwtException e) {
            return false; // 🔥 Token expired
        } catch (JwtException e) {
            return false; // 🔥 Invalid token
        }
    }
}
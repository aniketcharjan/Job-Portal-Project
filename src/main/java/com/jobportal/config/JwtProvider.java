package com.jobportal.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;

/**
 * JWT Provider Service
 * This service handles JWT token generation, validation, and extraction
 */
@Service
public class JwtProvider {
    
    // Secret key for signing JWT tokens
    private final SecretKey secretKey = Keys.hmacShaKeyFor(JwtConstant.SECRET_KEY.getBytes());

    /**
     * Generate JWT Token from Authentication
     * @param auth Spring Security Authentication object
     * @return JWT token string
     */
    public String generateToken(Authentication auth) {
        UserDetails userDetails = (UserDetails) auth.getPrincipal();
        return generateTokenFromEmail(userDetails.getUsername());
    }

    /**
     * Generate JWT Token from email
     * @param email User's email
     * @return JWT token string
     */
    public String generateTokenFromEmail(String email) {
        return Jwts.builder()
                .setSubject(email) // Set email as subject
                .setIssuer(JwtConstant.JWT_ISSUER) // Set issuer
                .setIssuedAt(new Date()) // Set issue date
                .setExpiration(new Date(System.currentTimeMillis() + JwtConstant.JWT_EXPIRATION)) // Set expiration
                .signWith(secretKey, SignatureAlgorithm.HS256) // Sign with secret key
                .compact();
    }

    /**
     * Extract email from JWT token
     * @param token JWT token string
     * @return email from token
     */
    public String getEmailFromJwtToken(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            return claims.getSubject();
        } catch (Exception e) {
            throw new RuntimeException("Invalid JWT token: " + e.getMessage());
        }
    }

    /**
     * Validate JWT token
     * @param token JWT token string
     * @return true if token is valid, false otherwise
     */
    public boolean validateJwtToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            System.err.println("JWT validation error: " + e.getMessage());
            return false;
        }
    }

    /**
     * Extract JWT token from Authorization header
     * @param authHeader Authorization header value
     * @return JWT token without "Bearer " prefix
     */
    public String extractTokenFromHeader(String authHeader) {
        if (authHeader != null && authHeader.startsWith(JwtConstant.JWT_PREFIX)) {
            return authHeader.substring(JwtConstant.JWT_PREFIX.length());
        }
        return null;
    }
}
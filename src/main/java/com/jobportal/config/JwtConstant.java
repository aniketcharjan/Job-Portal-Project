package com.jobportal.config;

/**
 * JWT Constants Class
 * This class contains all JWT related constants used throughout the application
 */
public class JwtConstant {
    
    /**
     * JWT Secret Key - used to sign and verify JWT tokens
     * In production, this should be stored in environment variables
     */
    public static final String SECRET_KEY = "mySecretKey12345678901234567890123456789012345678901234567890";
    
    /**
     * JWT Token Expiration Time (24 hours in milliseconds)
     */
    public static final long JWT_EXPIRATION = 86400000L;
    
    /**
     * JWT Token Prefix in Authorization Header
     */
    public static final String JWT_HEADER = "Authorization";
    
    /**
     * Bearer prefix for JWT tokens
     */
    public static final String JWT_PREFIX = "Bearer ";
    
    /**
     * Default JWT issuer
     */
    public static final String JWT_ISSUER = "JobPortal";
}

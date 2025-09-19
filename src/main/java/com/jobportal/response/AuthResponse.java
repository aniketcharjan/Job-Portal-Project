package com.jobportal.response;

/**
 * Authentication Response DTO
 * This class represents the response sent after successful login/signup
 */
public class AuthResponse {
    
    private String jwt; // JWT token
    private String message;
    private boolean success;
    private UserResponse user; // User details without password
    
    // Default constructor
    public AuthResponse() {}
    
    // Constructor for success response
    public AuthResponse(String jwt, String message, UserResponse user) {
        this.jwt = jwt;
        this.message = message;
        this.success = true;
        this.user = user;
    }
    
    // Constructor for error response
    public AuthResponse(String message, boolean success) {
        this.message = message;
        this.success = success;
    }
    
    // Getters and Setters
    public String getJwt() {
        return jwt;
    }
    
    public void setJwt(String jwt) {
        this.jwt = jwt;
    }
    
    public String getMessage() {
        return message;
    }
    
    public void setMessage(String message) {
        this.message = message;
    }
    
    public boolean isSuccess() {
        return success;
    }
    
    public void setSuccess(boolean success) {
        this.success = success;
    }
    
    public UserResponse getUser() {
        return user;
    }
    
    public void setUser(UserResponse user) {
        this.user = user;
    }
    
    @Override
    public String toString() {
        return "AuthResponse{" +
                "message='" + message + '\'' +
                ", success=" + success +
                ", user=" + (user != null ? user.getEmail() : "null") +
                '}';
    }
}
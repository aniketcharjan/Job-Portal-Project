package com.jobportal.exception;

/**
 * User Not Found Exception
 * Thrown when a requested user is not found in the database
 */
public class UserNotFoundException extends RuntimeException {
    
    public UserNotFoundException(String message) {
        super(message);
    }
    
    public UserNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
    
    // Static factory methods for common scenarios
    public static UserNotFoundException byEmail(String email) {
        return new UserNotFoundException("User not found with email: " + email);
    }
    
    public static UserNotFoundException byId(String id) {
        return new UserNotFoundException("User not found with ID: " + id);
    }
}
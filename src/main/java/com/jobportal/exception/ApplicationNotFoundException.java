package com.jobportal.exception;

/**
 * Application Not Found Exception
 * Thrown when a requested application is not found in the database
 */
public class ApplicationNotFoundException extends RuntimeException {
    
    public ApplicationNotFoundException(String message) {
        super(message);
    }
    
    public ApplicationNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
    
    // Static factory method for common scenario
    public static ApplicationNotFoundException byId(String id) {
        return new ApplicationNotFoundException("Application not found with ID: " + id);
    }
}
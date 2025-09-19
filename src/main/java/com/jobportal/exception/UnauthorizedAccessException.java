package com.jobportal.exception;

/**
 * Unauthorized Access Exception
 * Thrown when a user tries to access resources they don't have permission to
 */
public class UnauthorizedAccessException extends RuntimeException {
    
    public UnauthorizedAccessException(String message) {
        super(message);
    }
    
    public UnauthorizedAccessException(String message, Throwable cause) {
        super(message, cause);
    }
    
    // Static factory methods for common scenarios
    public static UnauthorizedAccessException forResource(String resource) {
        return new UnauthorizedAccessException("You are not authorized to access this " + resource);
    }
    
    public static UnauthorizedAccessException forAction(String action) {
        return new UnauthorizedAccessException("You are not authorized to perform this action: " + action);
    }
}
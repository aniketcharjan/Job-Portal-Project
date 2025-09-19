package com.jobportal.exception;

/**
 * Job Not Found Exception
 * Thrown when a requested job is not found in the database
 */
public class JobNotFoundException extends RuntimeException {
    
    public JobNotFoundException(String message) {
        super(message);
    }
    
    public JobNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
    
    // Static factory method for common scenario
    public static JobNotFoundException byId(String id) {
        return new JobNotFoundException("Job not found with ID: " + id);
    }
}
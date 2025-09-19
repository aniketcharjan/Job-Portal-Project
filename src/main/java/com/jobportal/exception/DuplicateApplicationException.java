package com.jobportal.exception;

/**
 * Duplicate Application Exception
 * Thrown when a job seeker tries to apply for the same job twice
 */
public class DuplicateApplicationException extends RuntimeException {
    
    public DuplicateApplicationException(String message) {
        super(message);
    }
    
    public DuplicateApplicationException(String message, Throwable cause) {
        super(message, cause);
    }
    
    // Static factory method for common scenario
    public static DuplicateApplicationException forJob(String jobTitle) {
        return new DuplicateApplicationException("You have already applied for the job: " + jobTitle);
    }
}
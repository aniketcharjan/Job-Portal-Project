package com.jobportal.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * Application Request DTO
 * This class represents the data received when a job seeker applies for a job
 */
public class ApplicationRequest {
    
    @NotNull(message = "Job ID is required")
    private String jobId;
    
    @NotBlank(message = "Cover letter is required")
    private String coverLetter;
    
    private String resumeUrl; // URL to uploaded resume
    private String expectedSalary;
    private String availabilityDate;
    private Boolean willingToRelocate;
    
    // Default constructor
    public ApplicationRequest() {
        this.willingToRelocate = false;
    }
    
    // Constructor
    public ApplicationRequest(String jobId, String coverLetter) {
        this();
        this.jobId = jobId;
        this.coverLetter = coverLetter;
    }
    
    // Getters and Setters
    public String getJobId() {
        return jobId;
    }
    
    public void setJobId(String jobId) {
        this.jobId = jobId;
    }
    
    public String getCoverLetter() {
        return coverLetter;
    }
    
    public void setCoverLetter(String coverLetter) {
        this.coverLetter = coverLetter;
    }
    
    public String getResumeUrl() {
        return resumeUrl;
    }
    
    public void setResumeUrl(String resumeUrl) {
        this.resumeUrl = resumeUrl;
    }
    
    public String getExpectedSalary() {
        return expectedSalary;
    }
    
    public void setExpectedSalary(String expectedSalary) {
        this.expectedSalary = expectedSalary;
    }
    
    public String getAvailabilityDate() {
        return availabilityDate;
    }
    
    public void setAvailabilityDate(String availabilityDate) {
        this.availabilityDate = availabilityDate;
    }
    
    public Boolean getWillingToRelocate() {
        return willingToRelocate;
    }
    
    public void setWillingToRelocate(Boolean willingToRelocate) {
        this.willingToRelocate = willingToRelocate;
    }
    
    @Override
    public String toString() {
        return "ApplicationRequest{" +
                "jobId='" + jobId + '\'' +
                ", expectedSalary='" + expectedSalary + '\'' +
                ", availabilityDate='" + availabilityDate + '\'' +
                ", willingToRelocate=" + willingToRelocate +
                '}';
    }
}

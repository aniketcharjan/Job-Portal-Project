package com.jobportal.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DBRef;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

/**
 * Application Entity Class
 * This represents a job application submitted by a job seeker for a specific job
 */
@Document(collection = "applications") // MongoDB collection name
public class Application {
    
    @Id
    private String id; // MongoDB generates this automatically
    
    // Reference to the job being applied for
    @DBRef
    @NotNull(message = "Job reference is required")
    private Job job;
    
    // Reference to the job seeker who applied
    @DBRef
    @NotNull(message = "Job seeker reference is required")
    private User jobSeeker;
    
    @NotBlank(message = "Cover letter is required")
    private String coverLetter;
    
    private String resumeUrl; // URL to the resume file
    
    private String status; // "PENDING", "REVIEWED", "SHORTLISTED", "REJECTED", "HIRED"
    
    // Employer feedback
    private String employerNotes; // Notes from employer about this application
    
    // Timestamps
    private LocalDateTime appliedAt;
    private LocalDateTime reviewedAt;
    private LocalDateTime updatedAt;
    
    // Additional fields
    private String expectedSalary;
    private String availabilityDate; // When can the candidate start
    private Boolean willingToRelocate;
    
    // Default constructor
    public Application() {
        this.appliedAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        this.status = "PENDING";
        this.willingToRelocate = false;
    }
    
    // Constructor with required fields
    public Application(Job job, User jobSeeker, String coverLetter) {
        this();
        this.job = job;
        this.jobSeeker = jobSeeker;
        this.coverLetter = coverLetter;
    }
    
    // Getters and Setters
    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    public Job getJob() {
        return job;
    }
    
    public void setJob(Job job) {
        this.job = job;
        this.updatedAt = LocalDateTime.now();
    }
    
    public User getJobSeeker() {
        return jobSeeker;
    }
    
    public void setJobSeeker(User jobSeeker) {
        this.jobSeeker = jobSeeker;
        this.updatedAt = LocalDateTime.now();
    }
    
    public String getCoverLetter() {
        return coverLetter;
    }
    
    public void setCoverLetter(String coverLetter) {
        this.coverLetter = coverLetter;
        this.updatedAt = LocalDateTime.now();
    }
    
    public String getResumeUrl() {
        return resumeUrl;
    }
    
    public void setResumeUrl(String resumeUrl) {
        this.resumeUrl = resumeUrl;
        this.updatedAt = LocalDateTime.now();
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
        this.updatedAt = LocalDateTime.now();
        
        // Set reviewedAt when status changes from PENDING
        if (!"PENDING".equals(status) && this.reviewedAt == null) {
            this.reviewedAt = LocalDateTime.now();
        }
    }
    
    public String getEmployerNotes() {
        return employerNotes;
    }
    
    public void setEmployerNotes(String employerNotes) {
        this.employerNotes = employerNotes;
        this.updatedAt = LocalDateTime.now();
    }
    
    public LocalDateTime getAppliedAt() {
        return appliedAt;
    }
    
    public void setAppliedAt(LocalDateTime appliedAt) {
        this.appliedAt = appliedAt;
    }
    
    public LocalDateTime getReviewedAt() {
        return reviewedAt;
    }
    
    public void setReviewedAt(LocalDateTime reviewedAt) {
        this.reviewedAt = reviewedAt;
    }
    
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
    
    public String getExpectedSalary() {
        return expectedSalary;
    }
    
    public void setExpectedSalary(String expectedSalary) {
        this.expectedSalary = expectedSalary;
        this.updatedAt = LocalDateTime.now();
    }
    
    public String getAvailabilityDate() {
        return availabilityDate;
    }
    
    public void setAvailabilityDate(String availabilityDate) {
        this.availabilityDate = availabilityDate;
        this.updatedAt = LocalDateTime.now();
    }
    
    public Boolean getWillingToRelocate() {
        return willingToRelocate;
    }
    
    public void setWillingToRelocate(Boolean willingToRelocate) {
        this.willingToRelocate = willingToRelocate;
        this.updatedAt = LocalDateTime.now();
    }
    
    // Helper methods
    public boolean isPending() {
        return "PENDING".equals(status);
    }
    
    public boolean isReviewed() {
        return "REVIEWED".equals(status);
    }
    
    public boolean isShortlisted() {
        return "SHORTLISTED".equals(status);
    }
    
    public boolean isRejected() {
        return "REJECTED".equals(status);
    }
    
    public boolean isHired() {
        return "HIRED".equals(status);
    }
    
    @Override
    public String toString() {
        return "Application{" +
                "id='" + id + '\'' +
                ", jobTitle='" + (job != null ? job.getTitle() : "N/A") + '\'' +
                ", applicantName='" + (jobSeeker != null ? jobSeeker.getFullName() : "N/A") + '\'' +
                ", status='" + status + '\'' +
                ", appliedAt=" + appliedAt +
                '}';
    }
}
package com.jobportal.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DBRef;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Job Entity Class
 * This represents a job posting in our job portal system
 */
@Document(collection = "jobs") // MongoDB collection name
public class Job {
    
    @Id
    private String id; // MongoDB generates this automatically
    
    @NotBlank(message = "Job title is required")
    private String title;
    
    @NotBlank(message = "Job description is required")
    private String description;
    
    @NotBlank(message = "Company name is required")
    private String companyName;
    
    @NotBlank(message = "Job location is required")
    private String location;
    
    @NotBlank(message = "Job type is required")
    private String jobType; // "FULL_TIME", "PART_TIME", "CONTRACT", "INTERNSHIP"
    
    @NotBlank(message = "Experience level is required")
    private String experienceLevel; // "FRESHER", "1-2 YEARS", "3-5 YEARS", "5+ YEARS"
    
    private Double salaryMin;
    private Double salaryMax;
    private String salaryCurrency; // "USD", "INR", etc.
    
    @NotNull(message = "Required skills are mandatory")
    private List<String> requiredSkills;
    
    private List<String> responsibilities;
    private List<String> requirements;
    private List<String> benefits;
    
    @NotBlank(message = "Application deadline is required")
    private String applicationDeadline;
    
    // Reference to the employer who posted this job
    @DBRef
    private User employer; // This will reference the User document
    
    private String status; // "ACTIVE", "CLOSED", "DRAFT"
    
    // SEO and search fields
    private List<String> tags; // For better search functionality
    private String category; // "IT", "Marketing", "Sales", etc.
    
    // Timestamps
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    // Application tracking
    private Integer totalApplications;
    private Integer viewCount;
    
    // Default constructor
    public Job() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        this.status = "ACTIVE";
        this.totalApplications = 0;
        this.viewCount = 0;
        this.salaryCurrency = "USD";
    }
    
    // Constructor with basic fields
    public Job(String title, String description, String companyName, String location, 
               String jobType, String experienceLevel, User employer) {
        this();
        this.title = title;
        this.description = description;
        this.companyName = companyName;
        this.location = location;
        this.jobType = jobType;
        this.experienceLevel = experienceLevel;
        this.employer = employer;
    }
    
    // Getters and Setters
    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
        this.updatedAt = LocalDateTime.now();
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
        this.updatedAt = LocalDateTime.now();
    }
    
    public String getCompanyName() {
        return companyName;
    }
    
    public void setCompanyName(String companyName) {
        this.companyName = companyName;
        this.updatedAt = LocalDateTime.now();
    }
    
    public String getLocation() {
        return location;
    }
    
    public void setLocation(String location) {
        this.location = location;
        this.updatedAt = LocalDateTime.now();
    }
    
    public String getJobType() {
        return jobType;
    }
    
    public void setJobType(String jobType) {
        this.jobType = jobType;
        this.updatedAt = LocalDateTime.now();
    }
    
    public String getExperienceLevel() {
        return experienceLevel;
    }
    
    public void setExperienceLevel(String experienceLevel) {
        this.experienceLevel = experienceLevel;
        this.updatedAt = LocalDateTime.now();
    }
    
    public Double getSalaryMin() {
        return salaryMin;
    }
    
    public void setSalaryMin(Double salaryMin) {
        this.salaryMin = salaryMin;
        this.updatedAt = LocalDateTime.now();
    }
    
    public Double getSalaryMax() {
        return salaryMax;
    }
    
    public void setSalaryMax(Double salaryMax) {
        this.salaryMax = salaryMax;
        this.updatedAt = LocalDateTime.now();
    }
    
    public String getSalaryCurrency() {
        return salaryCurrency;
    }
    
    public void setSalaryCurrency(String salaryCurrency) {
        this.salaryCurrency = salaryCurrency;
        this.updatedAt = LocalDateTime.now();
    }
    
    public List<String> getRequiredSkills() {
        return requiredSkills;
    }
    
    public void setRequiredSkills(List<String> requiredSkills) {
        this.requiredSkills = requiredSkills;
        this.updatedAt = LocalDateTime.now();
    }
    
    public List<String> getResponsibilities() {
        return responsibilities;
    }
    
    public void setResponsibilities(List<String> responsibilities) {
        this.responsibilities = responsibilities;
        this.updatedAt = LocalDateTime.now();
    }
    
    public List<String> getRequirements() {
        return requirements;
    }
    
    public void setRequirements(List<String> requirements) {
        this.requirements = requirements;
        this.updatedAt = LocalDateTime.now();
    }
    
    public List<String> getBenefits() {
        return benefits;
    }
    
    public void setBenefits(List<String> benefits) {
        this.benefits = benefits;
        this.updatedAt = LocalDateTime.now();
    }
    
    public String getApplicationDeadline() {
        return applicationDeadline;
    }
    
    public void setApplicationDeadline(String applicationDeadline) {
        this.applicationDeadline = applicationDeadline;
        this.updatedAt = LocalDateTime.now();
    }
    
    public User getEmployer() {
        return employer;
    }
    
    public void setEmployer(User employer) {
        this.employer = employer;
        this.updatedAt = LocalDateTime.now();
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
        this.updatedAt = LocalDateTime.now();
    }
    
    public List<String> getTags() {
        return tags;
    }
    
    public void setTags(List<String> tags) {
        this.tags = tags;
        this.updatedAt = LocalDateTime.now();
    }
    
    public String getCategory() {
        return category;
    }
    
    public void setCategory(String category) {
        this.category = category;
        this.updatedAt = LocalDateTime.now();
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
    
    public Integer getTotalApplications() {
        return totalApplications;
    }
    
    public void setTotalApplications(Integer totalApplications) {
        this.totalApplications = totalApplications;
        this.updatedAt = LocalDateTime.now();
    }
    
    public Integer getViewCount() {
        return viewCount;
    }
    
    public void setViewCount(Integer viewCount) {
        this.viewCount = viewCount;
        this.updatedAt = LocalDateTime.now();
    }
    
    // Helper methods
    public void incrementViewCount() {
        this.viewCount = (this.viewCount == null) ? 1 : this.viewCount + 1;
        this.updatedAt = LocalDateTime.now();
    }
    
    public void incrementApplicationCount() {
        this.totalApplications = (this.totalApplications == null) ? 1 : this.totalApplications + 1;
        this.updatedAt = LocalDateTime.now();
    }
    
    public String getSalaryRange() {
        if (salaryMin != null && salaryMax != null) {
            return salaryCurrency + " " + salaryMin + " - " + salaryMax;
        } else if (salaryMin != null) {
            return salaryCurrency + " " + salaryMin + "+";
        } else if (salaryMax != null) {
            return "Up to " + salaryCurrency + " " + salaryMax;
        }
        return "Salary not specified";
    }
    
    @Override
    public String toString() {
        return "Job{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", companyName='" + companyName + '\'' +
                ", location='" + location + '\'' +
                ", jobType='" + jobType + '\'' +
                ", status='" + status + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}
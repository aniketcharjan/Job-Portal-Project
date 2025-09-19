package com.jobportal.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

/**
 * Job Request DTO
 * This class represents the data received when creating or updating a job
 */
public class JobRequest {
    
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
    private String salaryCurrency;
    
    @NotNull(message = "Required skills are mandatory")
    private List<String> requiredSkills;
    
    private List<String> responsibilities;
    private List<String> requirements;
    private List<String> benefits;
    
    @NotBlank(message = "Application deadline is required")
    private String applicationDeadline;
    
    private List<String> tags;
    private String category;
    
    // Default constructor
    public JobRequest() {
        this.salaryCurrency = "USD";
    }
    
    // Getters and Setters
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public String getCompanyName() {
        return companyName;
    }
    
    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }
    
    public String getLocation() {
        return location;
    }
    
    public void setLocation(String location) {
        this.location = location;
    }
    
    public String getJobType() {
        return jobType;
    }
    
    public void setJobType(String jobType) {
        this.jobType = jobType;
    }
    
    public String getExperienceLevel() {
        return experienceLevel;
    }
    
    public void setExperienceLevel(String experienceLevel) {
        this.experienceLevel = experienceLevel;
    }
    
    public Double getSalaryMin() {
        return salaryMin;
    }
    
    public void setSalaryMin(Double salaryMin) {
        this.salaryMin = salaryMin;
    }
    
    public Double getSalaryMax() {
        return salaryMax;
    }
    
    public void setSalaryMax(Double salaryMax) {
        this.salaryMax = salaryMax;
    }
    
    public String getSalaryCurrency() {
        return salaryCurrency;
    }
    
    public void setSalaryCurrency(String salaryCurrency) {
        this.salaryCurrency = salaryCurrency;
    }
    
    public List<String> getRequiredSkills() {
        return requiredSkills;
    }
    
    public void setRequiredSkills(List<String> requiredSkills) {
        this.requiredSkills = requiredSkills;
    }
    
    public List<String> getResponsibilities() {
        return responsibilities;
    }
    
    public void setResponsibilities(List<String> responsibilities) {
        this.responsibilities = responsibilities;
    }
    
    public List<String> getRequirements() {
        return requirements;
    }
    
    public void setRequirements(List<String> requirements) {
        this.requirements = requirements;
    }
    
    public List<String> getBenefits() {
        return benefits;
    }
    
    public void setBenefits(List<String> benefits) {
        this.benefits = benefits;
    }
    
    public String getApplicationDeadline() {
        return applicationDeadline;
    }
    
    public void setApplicationDeadline(String applicationDeadline) {
        this.applicationDeadline = applicationDeadline;
    }
    
    public List<String> getTags() {
        return tags;
    }
    
    public void setTags(List<String> tags) {
        this.tags = tags;
    }
    
    public String getCategory() {
        return category;
    }
    
    public void setCategory(String category) {
        this.category = category;
    }
    
    @Override
    public String toString() {
        return "JobRequest{" +
                "title='" + title + '\'' +
                ", companyName='" + companyName + '\'' +
                ", location='" + location + '\'' +
                ", jobType='" + jobType + '\'' +
                ", experienceLevel='" + experienceLevel + '\'' +
                '}';
    }
}
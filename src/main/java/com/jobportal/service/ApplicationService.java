package com.jobportal.service;

import com.jobportal.exception.ApplicationNotFoundException;
import com.jobportal.exception.DuplicateApplicationException;
import com.jobportal.exception.JobNotFoundException;
import com.jobportal.exception.UnauthorizedAccessException;
import com.jobportal.model.Application;
import com.jobportal.model.Job;
import com.jobportal.model.User;
import com.jobportal.repository.ApplicationRepository;
import com.jobportal.repository.JobRepository;
import com.jobportal.request.ApplicationRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Application Service
 * This service handles job application operations like applying, reviewing applications, etc.
 */
@Service
public class ApplicationService {
    
    @Autowired
    private ApplicationRepository applicationRepository;
    
    @Autowired
    private JobRepository jobRepository;
    
    @Autowired
    private UserService userService;
    
    /**
     * Apply for a job (only job seekers can apply)
     * @param applicationRequest Application details
     * @return Created Application
     */
    public Application applyForJob(ApplicationRequest applicationRequest) {
        // Get current user and verify they are a job seeker
        User currentUser = userService.getCurrentUser();
        if (!currentUser.isJobSeeker()) {
            throw UnauthorizedAccessException.forAction("apply for jobs");
        }
        
        // Find the job
        Job job = jobRepository.findById(applicationRequest.getJobId())
                .orElseThrow(() -> JobNotFoundException.byId(applicationRequest.getJobId()));
        
        // Check if user has already applied for this job
        if (applicationRepository.existsByJobAndJobSeeker(job, currentUser)) {
            throw DuplicateApplicationException.forJob(job.getTitle());
        }
        
        // Check if job is still active
        if (!"ACTIVE".equals(job.getStatus())) {
            throw new IllegalArgumentException("Cannot apply to inactive job");
        }
        
        // Create new application
        Application application = new Application();
        application.setJob(job);
        application.setJobSeeker(currentUser);
        application.setCoverLetter(applicationRequest.getCoverLetter());
        application.setResumeUrl(applicationRequest.getResumeUrl());
        application.setExpectedSalary(applicationRequest.getExpectedSalary());
        application.setAvailabilityDate(applicationRequest.getAvailabilityDate());
        application.setWillingToRelocate(applicationRequest.getWillingToRelocate());
        
        // Save application
        Application savedApplication = applicationRepository.save(application);
        
        // Increment application count in job
        job.incrementApplicationCount();
        jobRepository.save(job);
        
        return savedApplication;
    }
    
    /**
     * Get applications by job seeker (my applications)
     * @param page Page number
     * @param size Page size
     * @return Page of applications by current job seeker
     */
    public Page<Application> getMyApplications(int page, int size) {
        User currentUser = userService.getCurrentUser();
        if (!currentUser.isJobSeeker()) {
            throw UnauthorizedAccessException.forAction("view job applications");
        }
        
        Pageable pageable = PageRequest.of(page, size, Sort.by("appliedAt").descending());
        return applicationRepository.findByJobSeeker(currentUser, pageable);
    }
    
    /**
     * Get applications for jobs posted by current employer
     * @param page Page number
     * @param size Page size
     * @return Page of applications for employer's jobs
     */
    public Page<Application> getApplicationsForMyJobs(int page, int size) {
        User currentUser = userService.getCurrentUser();
        if (!currentUser.isEmployer()) {
            throw UnauthorizedAccessException.forAction("view job applications");
        }
        
        Pageable pageable = PageRequest.of(page, size, Sort.by("appliedAt").descending());
        return applicationRepository.findApplicationsForEmployer(currentUser, pageable);
    }
    
    /**
     * Get applications for a specific job (only job owner can view)
     * @param jobId Job ID
     * @param page Page number
     * @param size Page size
     * @return Page of applications for the job
     */
    public Page<Application> getApplicationsForJob(String jobId, int page, int size) {
        // Find the job
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> JobNotFoundException.byId(jobId));
        
        // Verify current user owns this job
        User currentUser = userService.getCurrentUser();
        if (!job.getEmployer().getId().equals(currentUser.getId())) {
            throw UnauthorizedAccessException.forAction("view applications for this job");
        }
        
        Pageable pageable = PageRequest.of(page, size, Sort.by("appliedAt").descending());
        return applicationRepository.findByJob(job, pageable);
    }
    
    /**
     * Update application status (only employers can update)
     * @param applicationId Application ID
     * @param status New status
     * @param employerNotes Optional employer notes
     * @return Updated Application
     */
    public Application updateApplicationStatus(String applicationId, String status, String employerNotes) {
        // Find application
        Application application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> ApplicationNotFoundException.byId(applicationId));
        
        // Verify current user is the employer who posted the job
        User currentUser = userService.getCurrentUser();
        if (!application.getJob().getEmployer().getId().equals(currentUser.getId())) {
            throw UnauthorizedAccessException.forAction("update this application");
        }
        
        // Validate status
        if (!isValidStatus(status)) {
            throw new IllegalArgumentException("Invalid application status: " + status);
        }
        
        // Update application
        application.setStatus(status);
        if (employerNotes != null) {
            application.setEmployerNotes(employerNotes);
        }
        
        return applicationRepository.save(application);
    }
    
    /**
     * Get application by ID
     * @param applicationId Application ID
     * @return Application details
     */
    public Application getApplicationById(String applicationId) {
        Application application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> ApplicationNotFoundException.byId(applicationId));
        
        // Verify user has access to this application
        User currentUser = userService.getCurrentUser();
        boolean isJobSeeker = currentUser.getId().equals(application.getJobSeeker().getId());
        boolean isEmployer = currentUser.getId().equals(application.getJob().getEmployer().getId());
        
        if (!isJobSeeker && !isEmployer) {
            throw UnauthorizedAccessException.forAction("view this application");
        }
        
        return application;
    }
    
    /**
     * Get application statistics for job seeker
     * @return Application statistics
     */
    public ApplicationStats getApplicationStats() {
        User currentUser = userService.getCurrentUser();
        if (!currentUser.isJobSeeker()) {
            throw UnauthorizedAccessException.forAction("view application statistics");
        }
        
        long totalApplications = applicationRepository.countByJobSeeker(currentUser);
        long pendingApplications = applicationRepository.countByJobSeekerAndStatus(currentUser, "PENDING");
        long reviewedApplications = applicationRepository.countByJobSeekerAndStatus(currentUser, "REVIEWED");
        long shortlistedApplications = applicationRepository.countByJobSeekerAndStatus(currentUser, "SHORTLISTED");
        long rejectedApplications = applicationRepository.countByJobSeekerAndStatus(currentUser, "REJECTED");
        long hiredApplications = applicationRepository.countByJobSeekerAndStatus(currentUser, "HIRED");
        
        return new ApplicationStats(totalApplications, pendingApplications, reviewedApplications, 
                shortlistedApplications, rejectedApplications, hiredApplications);
    }
    
    /**
     * Get applications by status for current user
     * @param status Application status
     * @return List of applications with specified status
     */
    public List<Application> getApplicationsByStatus(String status) {
        User currentUser = userService.getCurrentUser();
        
        if (currentUser.isJobSeeker()) {
            // Job seeker viewing their applications by status
            return applicationRepository.findByJobSeeker(currentUser).stream()
                    .filter(app -> status.equals(app.getStatus()))
                    .toList();
        } else if (currentUser.isEmployer()) {
            // Employer viewing applications for their jobs by status
            return applicationRepository.findApplicationsForEmployer(currentUser).stream()
                    .filter(app -> status.equals(app.getStatus()))
                    .toList();
        }
        
        throw UnauthorizedAccessException.forAction("view applications");
    }
    
    /**
     * Withdraw application (job seeker can withdraw before review)
     * @param applicationId Application ID
     */
    public void withdrawApplication(String applicationId) {
        Application application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> ApplicationNotFoundException.byId(applicationId));
        
        // Verify current user owns this application
        User currentUser = userService.getCurrentUser();
        if (!application.getJobSeeker().getId().equals(currentUser.getId())) {
            throw UnauthorizedAccessException.forAction("withdraw this application");
        }
        
        // Can only withdraw pending applications
        if (!"PENDING".equals(application.getStatus())) {
            throw new IllegalArgumentException("Cannot withdraw application that has been reviewed");
        }
        
        // Delete application
        applicationRepository.delete(application);
        
        // Decrement application count in job
        Job job = application.getJob();
        if (job.getTotalApplications() > 0) {
            job.setTotalApplications(job.getTotalApplications() - 1);
            jobRepository.save(job);
        }
    }
    
    // Helper methods
    
    /**
     * Validate application status
     * @param status Status to validate
     * @return true if valid, false otherwise
     */
    private boolean isValidStatus(String status) {
        return "PENDING".equals(status) || 
               "REVIEWED".equals(status) || 
               "SHORTLISTED".equals(status) || 
               "REJECTED".equals(status) || 
               "HIRED".equals(status);
    }
    
    /**
     * Inner class for application statistics
     */
    public static class ApplicationStats {
        private long totalApplications;
        private long pendingApplications;
        private long reviewedApplications;
        private long shortlistedApplications;
        private long rejectedApplications;
        private long hiredApplications;
        
        public ApplicationStats(long totalApplications, long pendingApplications, long reviewedApplications,
                              long shortlistedApplications, long rejectedApplications, long hiredApplications) {
            this.totalApplications = totalApplications;
            this.pendingApplications = pendingApplications;
            this.reviewedApplications = reviewedApplications;
            this.shortlistedApplications = shortlistedApplications;
            this.rejectedApplications = rejectedApplications;
            this.hiredApplications = hiredApplications;
        }
        
        // Getters
        public long getTotalApplications() { return totalApplications; }
        public long getPendingApplications() { return pendingApplications; }
        public long getReviewedApplications() { return reviewedApplications; }
        public long getShortlistedApplications() { return shortlistedApplications; }
        public long getRejectedApplications() { return rejectedApplications; }
        public long getHiredApplications() { return hiredApplications; }
    }
    
    public List<Application> getApplicationsByJobId(String jobId) {
        return applicationRepository.findByJob_Id(jobId);
    }

    
}
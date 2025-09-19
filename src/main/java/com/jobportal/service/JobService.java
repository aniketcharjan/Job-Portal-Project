package com.jobportal.service;

import com.jobportal.exception.JobNotFoundException;
import com.jobportal.exception.UnauthorizedAccessException;
import com.jobportal.model.Job;
import com.jobportal.model.User;
import com.jobportal.repository.JobRepository;
import com.jobportal.request.JobRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Job Service
 * This service handles job-related operations like creating, updating, searching jobs
 */
@Service
public class JobService {
    
    @Autowired
    private JobRepository jobRepository;
    
    @Autowired
    private UserService userService;
    
    /**
     * Create a new job posting (only employers can create jobs)
     * @param jobRequest Job creation request
     * @return Created Job
     */
    public Job createJob(JobRequest jobRequest) {
        // Get current user and verify they are an employer
        User currentUser = userService.getCurrentUser();
        if (!currentUser.isEmployer()) {
            throw UnauthorizedAccessException.forAction("create job posting");
        }
        
        // Create new job
        Job job = new Job();
        job.setTitle(jobRequest.getTitle());
        job.setDescription(jobRequest.getDescription());
        job.setCompanyName(jobRequest.getCompanyName());
        job.setLocation(jobRequest.getLocation());
        job.setJobType(jobRequest.getJobType());
        job.setExperienceLevel(jobRequest.getExperienceLevel());
        job.setSalaryMin(jobRequest.getSalaryMin());
        job.setSalaryMax(jobRequest.getSalaryMax());
        job.setSalaryCurrency(jobRequest.getSalaryCurrency());
        job.setRequiredSkills(jobRequest.getRequiredSkills());
        job.setResponsibilities(jobRequest.getResponsibilities());
        job.setRequirements(jobRequest.getRequirements());
        job.setBenefits(jobRequest.getBenefits());
        job.setApplicationDeadline(jobRequest.getApplicationDeadline());
        job.setTags(jobRequest.getTags());
        job.setCategory(jobRequest.getCategory());
        job.setEmployer(currentUser); // Set the employer
        
        return jobRepository.save(job);
    }
    
    /**
     * Update an existing job posting
     * @param jobId Job ID to update
     * @param jobRequest Updated job data
     * @return Updated Job
     */
    public Job updateJob(String jobId, JobRequest jobRequest) {
        // Find existing job
        Job existingJob = jobRepository.findById(jobId)
                .orElseThrow(() -> JobNotFoundException.byId(jobId));
        
        // Get current user and verify they own this job
        User currentUser = userService.getCurrentUser();
        if (!existingJob.getEmployer().getId().equals(currentUser.getId())) {
            throw UnauthorizedAccessException.forAction("update this job");
        }
        
        // Update job fields
        existingJob.setTitle(jobRequest.getTitle());
        existingJob.setDescription(jobRequest.getDescription());
        existingJob.setCompanyName(jobRequest.getCompanyName());
        existingJob.setLocation(jobRequest.getLocation());
        existingJob.setJobType(jobRequest.getJobType());
        existingJob.setExperienceLevel(jobRequest.getExperienceLevel());
        existingJob.setSalaryMin(jobRequest.getSalaryMin());
        existingJob.setSalaryMax(jobRequest.getSalaryMax());
        existingJob.setSalaryCurrency(jobRequest.getSalaryCurrency());
        existingJob.setRequiredSkills(jobRequest.getRequiredSkills());
        existingJob.setResponsibilities(jobRequest.getResponsibilities());
        existingJob.setRequirements(jobRequest.getRequirements());
        existingJob.setBenefits(jobRequest.getBenefits());
        existingJob.setApplicationDeadline(jobRequest.getApplicationDeadline());
        existingJob.setTags(jobRequest.getTags());
        existingJob.setCategory(jobRequest.getCategory());
        
        return jobRepository.save(existingJob);
    }
    
    /**
     * Delete a job posting
     * @param jobId Job ID to delete
     */
    public void deleteJob(String jobId) {
        // Find existing job
        Job existingJob = jobRepository.findById(jobId)
                .orElseThrow(() -> JobNotFoundException.byId(jobId));
        
        // Get current user and verify they own this job
        User currentUser = userService.getCurrentUser();
        if (!existingJob.getEmployer().getId().equals(currentUser.getId())) {
            throw UnauthorizedAccessException.forAction("delete this job");
        }
        
        jobRepository.delete(existingJob);
    }
    
    /**
     * Get job by ID and increment view count
     * @param jobId Job ID
     * @return Job details
     */
    public Job getJobById(String jobId) {
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> JobNotFoundException.byId(jobId));
        
        // Increment view count
        job.incrementViewCount();
        jobRepository.save(job);
        
        return job;
    }
    
    /**
     * Get all active jobs with pagination
     * @param page Page number (0-based)
     * @param size Page size
     * @return Page of active jobs
     */
    public Page<Job> getAllActiveJobs(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return jobRepository.findByStatus("ACTIVE", pageable);
    }
    
    /**
     * Get jobs posted by current employer
     * @param page Page number
     * @param size Page size
     * @return Page of jobs posted by current employer
     */
    public Page<Job> getMyJobs(int page, int size) {
        User currentUser = userService.getCurrentUser();
        if (!currentUser.isEmployer()) {
            throw UnauthorizedAccessException.forAction("access job postings");
        }
        
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return jobRepository.findByEmployer(currentUser, pageable);
    }
    
    /**
     * Search jobs with multiple criteria
     * @param title Job title keywords
     * @param location Job location
     * @param jobType Job type
     * @param page Page number
     * @param size Page size
     * @return Page of matching jobs
     */
    
    //update on 26-08-25 time 11:20 
    public Page<Job> searchJobs(String title, String location, String jobType, int page, int size) {
    	if (title == null) title = "";
        if (location == null) location = "";
        if( jobType == null) jobType = "";
        
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return jobRepository.searchJobs(title, location, jobType, pageable);
    }
    
//    public Page<Job> searchJobs(String title, String location, String jobType, int page, int size) {
//        // if parameter is null or empty, match everything with ".*"
//        String searchTitle = (title == null || title.isBlank()) ? ".*" : title;
//        String searchLocation = (location == null || location.isBlank()) ? ".*" : location;
//        String searchJobType = (jobType == null || jobType.isBlank()) ? ".*" : jobType;
//        
//        if (title == null) title = "";
//        if (location == null) location = "";
//        if (jobType == null) jobType = "";
//
//        title = title.trim();
//        location = location.trim();
//        jobType = jobType.trim();
//        
//        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
//        return jobRepository.searchJobs(searchTitle, searchLocation, searchJobType, pageable);
//    }

    
    
    /**
     * Get jobs by required skills
     * @param skills List of skills
     * @return List of jobs requiring any of the specified skills
     */
    public List<Job> getJobsBySkills(List<String> skills) {
        return jobRepository.findBySkillsIn(skills);
    }
    
    /**
     * Get recent jobs for homepage
     * @param limit Number of jobs to return
     * @return List of recent jobs
     */
    public List<Job> getRecentJobs(int limit) {
        Pageable pageable = PageRequest.of(0, limit);
        Page<Job> jobPage = jobRepository.findRecentJobs(pageable);
        return jobPage.getContent();
    }
    
    /**
     * Get popular jobs (by view count)
     * @param limit Number of jobs to return
     * @return List of popular jobs
     */
    public List<Job> getPopularJobs(int limit) {
        Pageable pageable = PageRequest.of(0, limit);
        Page<Job> jobPage = jobRepository.findPopularJobs(pageable);
        return jobPage.getContent();
    }
    
    /**
     * Change job status (ACTIVE, CLOSED, DRAFT)
     * @param jobId Job ID
     * @param status New status
     * @return Updated job
     */
    public Job changeJobStatus(String jobId, String status) {
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> JobNotFoundException.byId(jobId));
        
        // Verify current user owns this job
        User currentUser = userService.getCurrentUser();
        if (!job.getEmployer().getId().equals(currentUser.getId())) {
            throw UnauthorizedAccessException.forAction("modify this job");
        }
        
        // Validate status
        if (!status.equals("ACTIVE") && !status.equals("CLOSED") && !status.equals("DRAFT")) {
            throw new IllegalArgumentException("Invalid job status: " + status);
        }
        
        job.setStatus(status);
        return jobRepository.save(job);
    }
    
    /**
     * Get job statistics for an employer
     * @return Job statistics
     */
    public JobStats getJobStats() {
        User currentUser = userService.getCurrentUser();
        if (!currentUser.isEmployer()) {
            throw UnauthorizedAccessException.forAction("access job statistics");
        }
        
        long totalJobs = jobRepository.countByEmployer(currentUser);
        long activeJobs = jobRepository.countByEmployerAndStatus(currentUser, "ACTIVE");
        long closedJobs = jobRepository.countByEmployerAndStatus(currentUser, "CLOSED");
        long draftJobs = jobRepository.countByEmployerAndStatus(currentUser, "DRAFT");
        
        return new JobStats(totalJobs, activeJobs, closedJobs, draftJobs);
    }
    
    /**
     * Inner class for job statistics
     */
    public static class JobStats {
        private long totalJobs;
        private long activeJobs;
        private long closedJobs;
        private long draftJobs;
        
        public JobStats(long totalJobs, long activeJobs, long closedJobs, long draftJobs) {
            this.totalJobs = totalJobs;
            this.activeJobs = activeJobs;
            this.closedJobs = closedJobs;
            this.draftJobs = draftJobs;
        }
        
        // Getters
        public long getTotalJobs() { return totalJobs; }
        public long getActiveJobs() { return activeJobs; }
        public long getClosedJobs() { return closedJobs; }
        public long getDraftJobs() { return draftJobs; }
    }
}
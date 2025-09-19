package com.jobportal.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jobportal.model.Application;
import com.jobportal.model.Job;
import com.jobportal.request.JobRequest;
import com.jobportal.response.ApiResponse;
import com.jobportal.service.ApplicationService;
import com.jobportal.service.JobService;

import jakarta.validation.Valid;

/**
 * Job Controller
 * This controller handles job-related endpoints like creating, updating, searching jobs
 */
@RestController
@RequestMapping("/api/jobs")
@CrossOrigin(origins = "http://localhost:3000")
public class JobController {
    
    @Autowired
    private JobService jobService;
    @Autowired
    private ApplicationService applicationService;
    
    /**
     * Create a new job posting (only employers)
     * POST /api/jobs/create
     */
    @PostMapping("/create")
    public ResponseEntity<ApiResponse<Job>> createJob(@Valid @RequestBody JobRequest jobRequest) {
        try {
            Job createdJob = jobService.createJob(jobRequest);
            ApiResponse<Job> response = ApiResponse.success("Job created successfully", createdJob);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
            
        } catch (Exception e) {
            ApiResponse<Job> response = ApiResponse.error("Failed to create job: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    /**
     * Update existing job posting (only job owner)
     * PUT /api/jobs/{jobId}
     */
    @PutMapping("/{jobId}")
    public ResponseEntity<ApiResponse<Job>> updateJob(
            @PathVariable String jobId,
            @Valid @RequestBody JobRequest jobRequest) {
        try {
            Job updatedJob = jobService.updateJob(jobId, jobRequest);
            ApiResponse<Job> response = ApiResponse.success("Job updated successfully", updatedJob);
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            ApiResponse<Job> response = ApiResponse.error("Failed to update job: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    /**
     * Delete job posting (only job owner)
     * DELETE /api/jobs/{jobId}
     */
    @DeleteMapping("/{jobId}")
    public ResponseEntity<ApiResponse<String>> deleteJob(@PathVariable String jobId) {
        try {
            jobService.deleteJob(jobId);
            ApiResponse<String> response = ApiResponse.success("Job deleted successfully");
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            ApiResponse<String> response = ApiResponse.error("Failed to delete job: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    /**
     * Get job by ID (public access)
     * GET /api/jobs/public/{jobId}
     */
    @GetMapping("/public/{jobId}")
    public ResponseEntity<ApiResponse<Job>> getJobById(@PathVariable String jobId) {
        try {
            Job job = jobService.getJobById(jobId);
            ApiResponse<Job> response = ApiResponse.success("Job retrieved", job);
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            ApiResponse<Job> response = ApiResponse.error("Job not found: " + e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }
    
    /**
     * Get all active jobs with pagination (public access)
     * GET /api/jobs/public/all
     */
    @GetMapping("/public/all")
    public ResponseEntity<ApiResponse<Page<Job>>> getAllActiveJobs(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            Page<Job> jobs = jobService.getAllActiveJobs(page, size);
            ApiResponse<Page<Job>> response = ApiResponse.success("Jobs retrieved", jobs);
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            ApiResponse<Page<Job>> response = ApiResponse.error("Failed to get jobs: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    /**
     * Get jobs posted by current employer
     * GET /api/jobs/my-jobs
     */
    @GetMapping("/my-jobs")
    public ResponseEntity<ApiResponse<Page<Job>>> getMyJobs(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            Page<Job> jobs = jobService.getMyJobs(page, size);
            ApiResponse<Page<Job>> response = ApiResponse.success("My jobs retrieved", jobs);
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            ApiResponse<Page<Job>> response = ApiResponse.error("Failed to get jobs: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    /**
     * Search jobs with filters (public access)
     * GET /api/jobs/public/search
     */
    
    @GetMapping("/public/search")
    public ResponseEntity<ApiResponse<Page<Job>>> searchJobs(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String location,
            @RequestParam(required = false) String jobType,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
    	
    	
    	System.out.println("Search Params - Title: " + title + ", Location: " + location );
    	
    	
    	 
        try {
        	System.out.println("Searching jobs with title:1111111111111111111111111111111111111111 ");
            Page<Job> jobs = jobService.searchJobs(title, location, jobType, page, size);
            System.out.println("Searching jobs with title:22222222222222222222222222222222222222222 ");
            ApiResponse<Page<Job>> response = ApiResponse.success("Search results", jobs);
            System.out.println("Jobs found: " + jobs.getTotalElements());
            return ResponseEntity.ok(response);
            
            
            
        } catch (Exception e) {
            ApiResponse<Page<Job>> response = ApiResponse.error("Search failed: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    /**
     * Get recent jobs for homepage (public access)
     * GET /api/jobs/public/recent
     */
    @GetMapping("/public/recent")
    public ResponseEntity<ApiResponse<List<Job>>> getRecentJobs(@RequestParam(defaultValue = "10") int limit) {
        try {
            List<Job> jobs = jobService.getRecentJobs(limit);
            ApiResponse<List<Job>> response = ApiResponse.success("Recent jobs retrieved", jobs);
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            ApiResponse<List<Job>> response = ApiResponse.error("Failed to get recent jobs: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
    
//    gettitlebyjobId (String jobId)
//    {
//		return jobService.getJobById(jobId).getTitle();
//	}
    
    /**
     * Get popular jobs (public access)
     * GET /api/jobs/public/popular
     */
    @GetMapping("/public/popular")
    public ResponseEntity<ApiResponse<List<Job>>> getPopularJobs(@RequestParam(defaultValue = "10") int limit) {
        try {
            List<Job> jobs = jobService.getPopularJobs(limit);
            ApiResponse<List<Job>> response = ApiResponse.success("Popular jobs retrieved", jobs);
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            ApiResponse<List<Job>> response = ApiResponse.error("Failed to get popular jobs: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    /**
     * Change job status
     * PATCH /api/jobs/{jobId}/status
     */
    @PatchMapping("/{jobId}/status")
    public ResponseEntity<ApiResponse<Job>> changeJobStatus(
            @PathVariable String jobId,
            @RequestParam String status) {
        try {
            Job updatedJob = jobService.changeJobStatus(jobId, status);
            ApiResponse<Job> response = ApiResponse.success("Job status updated", updatedJob);
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            ApiResponse<Job> response = ApiResponse.error("Failed to update job status: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    /**
     * Get job statistics for current employer
     * GET /api/jobs/stats
     */
    @GetMapping("/stats")
    public ResponseEntity<ApiResponse<JobService.JobStats>> getJobStats() {
        try {
            JobService.JobStats stats = jobService.getJobStats();
            ApiResponse<JobService.JobStats> response = ApiResponse.success("Job statistics retrieved", stats);
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            ApiResponse<JobService.JobStats> response = ApiResponse.error("Failed to get job stats: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
    
 // JobController.java
    @GetMapping("/{jobId}/applicants")
    public ResponseEntity<ApiResponse<List<Application>>> getJobApplicants(@PathVariable String jobId) {
    	
    	System.out.println("Fetching applicants for job ID: " + jobId);
        try {
            List<Application> applicants = applicationService.getApplicationsByJobId(jobId);
            ApiResponse<List<Application>> response = ApiResponse.success("Applicants fetched successfully", applicants);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            ApiResponse<List<Application>> response = ApiResponse.error("Failed to fetch applicants: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

}
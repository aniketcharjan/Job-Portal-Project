package com.jobportal.controller;

import com.jobportal.model.Application;
import com.jobportal.request.ApplicationRequest;
import com.jobportal.response.ApiResponse;
import com.jobportal.service.ApplicationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Application Controller
 * This controller handles application-related endpoints like applying for jobs, managing applications
 */
@RestController
@RequestMapping("/api/applications")
@CrossOrigin(origins = "http://localhost:3000",
   methods = { RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.PATCH, RequestMethod.DELETE, RequestMethod.OPTIONS }
)
public class ApplicationController {
    
    @Autowired
    private ApplicationService applicationService;
    
    /**
     * Apply for a job (only job seekers)
     * POST /api/applications/apply
     */
    @PostMapping("/apply")
    public ResponseEntity<ApiResponse<Application>> applyForJob(@Valid @RequestBody ApplicationRequest applicationRequest) {
        try {
            Application application = applicationService.applyForJob(applicationRequest);
            ApiResponse<Application> response = ApiResponse.success("Application submitted successfully", application);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
            
        } catch (Exception e) {
            ApiResponse<Application> response = ApiResponse.error("Failed to submit application: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    /**
     * Get my applications (for job seekers)
     * GET /api/applications/my-applications
     */
    @GetMapping("/my-applications")
    public ResponseEntity<ApiResponse<Page<Application>>> getMyApplications(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            Page<Application> applications = applicationService.getMyApplications(page, size);
            ApiResponse<Page<Application>> response = ApiResponse.success("Applications retrieved", applications);
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            ApiResponse<Page<Application>> response = ApiResponse.error("Failed to get applications: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    /**
     * Get applications for my jobs (for employers)
     * GET /api/applications/my-job-applications
     */
    @GetMapping("/my-job-applications")
    public ResponseEntity<ApiResponse<Page<Application>>> getApplicationsForMyJobs(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            Page<Application> applications = applicationService.getApplicationsForMyJobs(page, size);
            ApiResponse<Page<Application>> response = ApiResponse.success("Applications retrieved", applications);
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            ApiResponse<Page<Application>> response = ApiResponse.error("Failed to get applications: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    /**
     * Get applications for a specific job (only job owner)
     * GET /api/applications/job/{jobId}
     */
    @GetMapping("/job/{jobId}")
    public ResponseEntity<ApiResponse<Page<Application>>> getApplicationsForJob(
            @PathVariable String jobId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            Page<Application> applications = applicationService.getApplicationsForJob(jobId, page, size);
            ApiResponse<Page<Application>> response = ApiResponse.success("Applications retrieved", applications);
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            ApiResponse<Page<Application>> response = ApiResponse.error("Failed to get applications: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    /**
     * Update application status (only employers)
     * PATCH /api/applications/{applicationId}/status
     */
    @PatchMapping("/{applicationId}/status")
    public ResponseEntity<ApiResponse<Application>> updateApplicationStatus(
            @PathVariable String applicationId,
            @RequestParam String status,
            @RequestParam(required = false) String employerNotes) {
        try {
            Application application = applicationService.updateApplicationStatus(applicationId, status, employerNotes);
            ApiResponse<Application> response = ApiResponse.success("Application status updated", application);
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            ApiResponse<Application> response = ApiResponse.error("Failed to update application: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    /**
     * Get application by ID
     * GET /api/applications/{applicationId}
     */
    @GetMapping("/{applicationId}")
    public ResponseEntity<ApiResponse<Application>> getApplicationById(@PathVariable String applicationId) {
        try {
            Application application = applicationService.getApplicationById(applicationId);
            ApiResponse<Application> response = ApiResponse.success("Application retrieved", application);
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            ApiResponse<Application> response = ApiResponse.error("Application not found: " + e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }
    
    /**
     * Get application statistics (for job seekers)
     * GET /api/applications/stats
     */
    @GetMapping("/stats")
    public ResponseEntity<ApiResponse<ApplicationService.ApplicationStats>> getApplicationStats() {
        try {
            ApplicationService.ApplicationStats stats = applicationService.getApplicationStats();
            ApiResponse<ApplicationService.ApplicationStats> response = ApiResponse.success("Application statistics retrieved", stats);
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            ApiResponse<ApplicationService.ApplicationStats> response = ApiResponse.error("Failed to get application stats: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    /**
     * Get applications by status
     * GET /api/applications/status/{status}
     */
    @GetMapping("/status/{status}")
    public ResponseEntity<ApiResponse<List<Application>>> getApplicationsByStatus(@PathVariable String status) {
        try {
            List<Application> applications = applicationService.getApplicationsByStatus(status);
            ApiResponse<List<Application>> response = ApiResponse.success("Applications retrieved", applications);
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            ApiResponse<List<Application>> response = ApiResponse.error("Failed to get applications: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    /**
     * Withdraw application (job seekers can withdraw pending applications)
     * DELETE /api/applications/{applicationId}/withdraw
     */
    @DeleteMapping("/{applicationId}/withdraw")
    public ResponseEntity<ApiResponse<String>> withdrawApplication(@PathVariable String applicationId) {
        try {
            applicationService.withdrawApplication(applicationId);
            ApiResponse<String> response = ApiResponse.success("Application withdrawn successfully");
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            ApiResponse<String> response = ApiResponse.error("Failed to withdraw application: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    
    
    
}
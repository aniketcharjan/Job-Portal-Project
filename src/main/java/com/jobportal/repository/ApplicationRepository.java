package com.jobportal.repository;

import com.jobportal.model.Application;
import com.jobportal.model.Job;
import com.jobportal.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Application Repository Interface
 * This interface provides database operations for Application entity
 */
@Repository
public interface ApplicationRepository extends MongoRepository<Application, String> {
    
    /**
     * Find applications by job seeker
     * @param jobSeeker The job seeker who applied
     * @return List<Application> - List of applications by the job seeker
     */
    List<Application> findByJobSeeker(User jobSeeker);
    
    /**
     * Find applications by job seeker with pagination
     * @param jobSeeker The job seeker who applied
     * @param pageable Pagination information
     * @return Page<Application> - Paginated list of applications
     */
    Page<Application> findByJobSeeker(User jobSeeker, Pageable pageable);
    
    /**
     * Find applications for a specific job
     * @param job The job for which applications are made
     * @return List<Application> - List of applications for the job
     */
    List<Application> findByJob(Job job);
    
    /**
     * Find applications for a specific job with pagination
     * @param job The job for which applications are made
     * @param pageable Pagination information
     * @return Page<Application> - Paginated list of applications
     */
    Page<Application> findByJob(Job job, Pageable pageable);
    
    /**
     * Find applications by status
     * @param status Application status
     * @return List<Application> - List of applications with specified status
     */
    List<Application> findByStatus(String status);
    
    /**
     * Find applications for jobs posted by a specific employer
     * @param employer The employer who posted the jobs
     * @return List<Application> - List of applications for employer's jobs
     */
    @Query("{ 'job.employer': ?0 }")
    List<Application> findApplicationsForEmployer(User employer);
    
    /**
     * Find applications for jobs posted by a specific employer with pagination
     * @param employer The employer who posted the jobs
     * @param pageable Pagination information
     * @return Page<Application> - Paginated list of applications for employer's jobs
     */
    @Query("{ 'job.employer': ?0 }")
    Page<Application> findApplicationsForEmployer(User employer, Pageable pageable);
    
    /**
     * Check if job seeker has already applied for a specific job
     * @param job The job
     * @param jobSeeker The job seeker
     * @return boolean - true if already applied, false otherwise
     */
    boolean existsByJobAndJobSeeker(Job job, User jobSeeker);
    
    /**
     * Find application by job and job seeker (to prevent duplicate applications)
     * @param job The job
     * @param jobSeeker The job seeker
     * @return Optional<Application> - Application if found
     */
    Optional<Application> findByJobAndJobSeeker(Job job, User jobSeeker);
    
    /**
     * Count applications by job seeker
     * @param jobSeeker The job seeker
     * @return long - Number of applications made by job seeker
     */
    long countByJobSeeker(User jobSeeker);
    
    /**
     * Count applications for a specific job
     * @param job The job
     * @return long - Number of applications for the job
     */
    long countByJob(Job job);
    
    /**
     * Count applications by status for a job seeker
     * @param jobSeeker The job seeker
     * @param status Application status
     * @return long - Number of applications with specified status
     */
    long countByJobSeekerAndStatus(User jobSeeker, String status);
    
    /**
     * Find applications by job and status
     * @param job The job
     * @param status Application status
     * @return List<Application> - List of applications with specified status for the job
     */
    List<Application> findByJobAndStatus(Job job, String status);
    
    /**
     * Find recent applications for an employer
     * @param employer The employer
     * @param pageable Pagination information
     * @return Page<Application> - Recent applications for employer's jobs
     */
    @Query(value = "{ 'job.employer': ?0 }", sort = "{ 'appliedAt': -1 }")
    Page<Application> findRecentApplicationsForEmployer(User employer, Pageable pageable);
    
    /**
     * Find recent applications by job seeker
     * @param jobSeeker The job seeker
     * @param pageable Pagination information
     * @return Page<Application> - Recent applications by job seeker
     */
    @Query(value = "{ 'jobSeeker': ?0 }", sort = "{ 'appliedAt': -1 }")
    Page<Application> findRecentApplicationsByJobSeeker(User jobSeeker, Pageable pageable);
    
    List<Application> findByJob_Id(String jobId);

    
}
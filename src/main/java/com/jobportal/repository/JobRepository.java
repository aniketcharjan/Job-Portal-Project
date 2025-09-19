package com.jobportal.repository;

import com.jobportal.model.Job;
import com.jobportal.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Job Repository Interface
 * This interface provides database operations for Job entity
 */
@Repository
public interface JobRepository extends MongoRepository<Job, String> {
    
    /**
     * Find jobs by employer
     * @param employer The employer who posted the jobs
     * @return List<Job> - List of jobs posted by the employer
     */
    List<Job> findByEmployer(User employer);
    
    /**
     * Find jobs by employer with pagination
     * @param employer The employer who posted the jobs
     * @param pageable Pagination information
     * @return Page<Job> - Paginated list of jobs
     */
    Page<Job> findByEmployer(User employer, Pageable pageable);
    
    /**
     * Find active jobs (status = "ACTIVE")
     * @return List<Job> - List of active jobs
     */
    List<Job> findByStatus(String status);
    
    /**
     * Find active jobs with pagination
     * @param status Job status
     * @param pageable Pagination information
     * @return Page<Job> - Paginated list of active jobs
     */
    Page<Job> findByStatus(String status, Pageable pageable);
    
    /**
     * Find jobs by job type (FULL_TIME, PART_TIME, etc.)
     * @param jobType Type of job
     * @return List<Job> - List of jobs of specified type
     */
    List<Job> findByJobType(String jobType);
    
    /**
     * Find jobs by experience level
     * @param experienceLevel Required experience level
     * @return List<Job> - List of jobs for specified experience level
     */
    List<Job> findByExperienceLevel(String experienceLevel);
    
    /**
     * Find jobs by location
     * @param location Job location
     * @return List<Job> - List of jobs in specified location
     */
    List<Job> findByLocationContainingIgnoreCase(String location);
    
    /**
     * Find jobs by company name
     * @param companyName Company name
     * @return List<Job> - List of jobs from specified company
     */
    List<Job> findByCompanyNameContainingIgnoreCase(String companyName);
    
    /**
     * Find jobs by title (search functionality)
     * @param title Job title keywords
     * @return List<Job> - List of jobs matching title
     */
    List<Job> findByTitleContainingIgnoreCase(String title);
    
    /**
     * Find jobs by required skills
     * @param skill Required skill
     * @return List<Job> - List of jobs requiring specified skill
     */
    @Query("{ 'requiredSkills': { $regex: ?0, $options: 'i' } }")
    List<Job> findByRequiredSkillsContaining(String skill);
    
    /**
     * Find jobs by category
     * @param category Job category
     * @return List<Job> - List of jobs in specified category
     */
    List<Job> findByCategory(String category);
    
    /**
     * Find jobs by salary range
     * @param minSalary Minimum salary
     * @param maxSalary Maximum salary
     * @return List<Job> - List of jobs within salary range
     */
    @Query("{ $or: [ " +
           "{ $and: [ { 'salaryMin': { $lte: ?1 } }, { 'salaryMax': { $gte: ?0 } } ] }, " +
           "{ $and: [ { 'salaryMin': { $exists: false } }, { 'salaryMax': { $exists: false } } ] } ] }")
    List<Job> findBySalaryRange(Double minSalary, Double maxSalary);
    
    /**
     * Search jobs with multiple criteria
     * @param title Job title keywords (optional)
     * @param location Job location (optional)
     * @param jobType Job type (optional)
     * @param pageable Pagination information
     * @return Page<Job> - Paginated search results
     */
    
    //change on 26-08-2025
    @Query("{ $and: [ " +
    	       "{ $or: [ { 'title': { $regex: ?0, $options: 'i' } }, { $expr: { $eq: [ ?0, null ] } } ] }, " +
    	       "{ $or: [ { 'location': { $regex: ?1, $options: 'i' } }, { $expr: { $eq: [ ?1, null ] } } ] }, " +
    	       "{ $or: [ { 'jobType': { $regex: ?2, $options: 'i' } }, { $expr: { $eq: [ ?2, null ] } } ] }, " +
    	       "{ 'status': 'ACTIVE' } ] }")
    	Page<Job> searchJobs(String title, String location, String jobType, Pageable pageable);

    
//    @Query("{ $and: [ " +
//           "{ $or: [ { 'title': { $regex: ?0, $options: 'i' } }, { $expr: { $eq: [ ?0, null ] } } ] }, " +
//           "{ $or: [ { 'location': { $regex: ?1, $options: 'i' } }, { $expr: { $eq: [ ?1, null ] } } ] }, " +
//           "{ $or: [ { 'jobType': ?2 }, { $expr: { $eq: [ ?2, null ] } } ] }, " +
//           "{ 'status': 'ACTIVE' } ] }")
//    Page<Job> searchJobs(String title, String location, String jobType, Pageable pageable);
    
    /**
     * Find recent jobs (for homepage)
     * @param pageable Pagination information (usually limit to 10)
     * @return Page<Job> - Recent active jobs
     */
    @Query(value = "{ 'status': 'ACTIVE' }", sort = "{ 'createdAt': -1 }")
    Page<Job> findRecentJobs(Pageable pageable);
    
    /**
     * Find popular jobs (by view count)
     * @param pageable Pagination information
     * @return Page<Job> - Popular jobs sorted by view count
     */
    @Query(value = "{ 'status': 'ACTIVE' }", sort = "{ 'viewCount': -1 }")
    Page<Job> findPopularJobs(Pageable pageable);
    
    /**
     * Find jobs by multiple skills
     * @param skills List of skills to match
     * @return List<Job> - Jobs that require any of the specified skills
     */
    @Query("{ 'requiredSkills': { $in: ?0 }, 'status': 'ACTIVE' }")
    List<Job> findBySkillsIn(List<String> skills);
    
    /**
     * Count jobs by employer
     * @param employer The employer
     * @return long - Number of jobs posted by employer
     */
    long countByEmployer(User employer);
    
    /**
     * Count active jobs by employer
     * @param employer The employer
     * @return long - Number of active jobs posted by employer
     */
    long countByEmployerAndStatus(User employer, String status);
}
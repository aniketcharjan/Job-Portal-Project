package com.jobportal.repository;

import com.jobportal.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * User Repository Interface
 * This interface provides database operations for User entity
 * MongoRepository provides basic CRUD operations automatically
 */
@Repository
public interface UserRepository extends MongoRepository<User, String> {
    
    /**
     * Find user by email (for login and authentication)
     * @param email User's email address
     * @return Optional<User> - User if found, empty if not found
     */
    Optional<User> findByEmail(String email);
    
    /**
     * Check if user exists by email (for registration validation)
     * @param email User's email address
     * @return boolean - true if user exists, false otherwise
     */
    boolean existsByEmail(String email);
    
    /**
     * Find users by role (to get all employers or job seekers)
     * @param role User role ("EMPLOYER" or "JOB_SEEKER")
     * @return List<User> - List of users with specified role
     */
    List<User> findByRole(String role);
    
    /**
     * Find users by company name (for employer search)
     * @param companyName Company name to search for
     * @return List<User> - List of users from specified company
     */
    List<User> findByCompanyNameContainingIgnoreCase(String companyName);
    
    /**
     * Find job seekers by skills (for employer to search candidates)
     * @param skill Skill to search for
     * @return List<User> - List of job seekers with specified skill
     */
    @Query("{ 'role': 'JOB_SEEKER', 'skills': { $regex: ?0, $options: 'i' } }")
    List<User> findJobSeekersBySkill(String skill);
    
    /**
     * Find job seekers by experience level
     * @param experience Experience level to search for
     * @return List<User> - List of job seekers with specified experience
     */
    @Query("{ 'role': 'JOB_SEEKER', 'experience': ?0 }")
    List<User> findJobSeekersByExperience(String experience);
    
    /**
     * Find users by location (city)
     * @param city City to search for
     * @return List<User> - List of users in specified city
     */
    List<User> findByCityContainingIgnoreCase(String city);
    
    /**
     * Find employers by industry
     * @param industry Industry to search for
     * @return List<User> - List of employers in specified industry
     */
    @Query("{ 'role': 'EMPLOYER', 'industry': { $regex: ?0, $options: 'i' } }")
    List<User> findEmployersByIndustry(String industry);
    
    /**
     * Search users by name (first name or last name)
     * @param name Name to search for
     * @return List<User> - List of users matching the name
     */
    @Query("{ $or: [ " +
           "{ 'firstName': { $regex: ?0, $options: 'i' } }, " +
           "{ 'lastName': { $regex: ?0, $options: 'i' } } ] }")
    List<User> findByNameContaining(String name);
    
    /**
     * Find all employers
     * @return List<User> - List of all employers
     */
    @Query("{ 'role': 'EMPLOYER' }")
    List<User> findAllEmployers();
    
    /**
     * Find all job seekers
     * @return List<User> - List of all job seekers
     */
    @Query("{ 'role': 'JOB_SEEKER' }")
    List<User> findAllJobSeekers();
    
    /**
     * Find job seekers who have uploaded resume
     * @return List<User> - List of job seekers with resume
     */
    @Query("{ 'role': 'JOB_SEEKER', 'resume': { $exists: true, $ne: null } }")
    List<User> findJobSeekersWithResume();
}
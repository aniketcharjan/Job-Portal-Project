package com.jobportal.service;

import com.jobportal.exception.UnauthorizedAccessException;
import com.jobportal.exception.UserNotFoundException;
import com.jobportal.model.User;
import com.jobportal.repository.UserRepository;
import com.jobportal.request.UpdateProfileRequest;
import com.jobportal.response.UserResponse;
import com.jobportal.dtoMapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * User Service
 * This service handles user-related operations like profile management, user search, etc.
 */
@Service
public class UserService {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private UserMapper userMapper;
    
    /**
     * Get user profile by ID
     * @param userId User ID
     * @return UserResponse
     */
    public UserResponse getUserProfile(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> UserNotFoundException.byId(userId));
        
        return userMapper.toUserResponse(user);
    }
    
    /**
     * Update user profile
     * @param userId User ID
     * @param updateRequest Profile update data
     * @return Updated UserResponse
     */
    public UserResponse updateProfile(String userId, UpdateProfileRequest updateRequest) {
        // Get current authenticated user
        String currentUserEmail = getCurrentUserEmail();
        User currentUser = userRepository.findByEmail(currentUserEmail)
                .orElseThrow(() -> UserNotFoundException.byEmail(currentUserEmail));
        
        // Check if user is updating their own profile
        if (!currentUser.getId().equals(userId)) {
            throw UnauthorizedAccessException.forAction("update profile");
        }
        
        // Update user fields
        if (updateRequest.getFirstName() != null) {
            currentUser.setFirstName(updateRequest.getFirstName());
        }
        if (updateRequest.getLastName() != null) {
            currentUser.setLastName(updateRequest.getLastName());
        }
        if (updateRequest.getPhone() != null) {
            currentUser.setPhone(updateRequest.getPhone());
        }
        if (updateRequest.getAddress() != null) {
            currentUser.setAddress(updateRequest.getAddress());
        }
        if (updateRequest.getCity() != null) {
            currentUser.setCity(updateRequest.getCity());
        }
        if (updateRequest.getState() != null) {
            currentUser.setState(updateRequest.getState());
        }
        if (updateRequest.getCountry() != null) {
            currentUser.setCountry(updateRequest.getCountry());
        }
        if (updateRequest.getProfilePicture() != null) {
            currentUser.setProfilePicture(updateRequest.getProfilePicture());
        }
        
        // Update job seeker specific fields
        if (currentUser.isJobSeeker()) {
            if (updateRequest.getResume() != null) {
                currentUser.setResume(updateRequest.getResume());
            }
            if (updateRequest.getSkills() != null) {
                currentUser.setSkills(updateRequest.getSkills());
            }
            if (updateRequest.getExperience() != null) {
                currentUser.setExperience(updateRequest.getExperience());
            }
            if (updateRequest.getEducation() != null) {
                currentUser.setEducation(updateRequest.getEducation());
            }
            if (updateRequest.getBio() != null) {
                currentUser.setBio(updateRequest.getBio());
            }
        }
        
        // Update employer specific fields
        if (currentUser.isEmployer()) {
            if (updateRequest.getCompanyName() != null) {
                currentUser.setCompanyName(updateRequest.getCompanyName());
            }
            if (updateRequest.getCompanyDescription() != null) {
                currentUser.setCompanyDescription(updateRequest.getCompanyDescription());
            }
            if (updateRequest.getWebsite() != null) {
                currentUser.setWebsite(updateRequest.getWebsite());
            }
            if (updateRequest.getCompanySize() != null) {
                currentUser.setCompanySize(updateRequest.getCompanySize());
            }
            if (updateRequest.getIndustry() != null) {
                currentUser.setIndustry(updateRequest.getIndustry());
            }
        }
        
        // Save updated user
        User updatedUser = userRepository.save(currentUser);
        
        return userMapper.toUserResponse(updatedUser);
    }
    
    /**
     * Search job seekers by skills (for employers)
     * @param skill Skill to search for
     * @return List of job seekers with the skill
     */
    public List<UserResponse> searchJobSeekersBySkill(String skill) {
        // Only employers can search job seekers
        checkEmployerAccess();
        
        List<User> jobSeekers = userRepository.findJobSeekersBySkill(skill);
        return jobSeekers.stream()
                .map(userMapper::toUserResponse)
                .collect(Collectors.toList());
    }
    
    /**
     * Get all job seekers (for employers)
     * @return List of all job seekers
     */
    public List<UserResponse> getAllJobSeekers() {
        // Only employers can view all job seekers
        checkEmployerAccess();
        
        List<User> jobSeekers = userRepository.findAllJobSeekers();
        return jobSeekers.stream()
                .map(userMapper::toUserResponse)
                .collect(Collectors.toList());
    }
    
    /**
     * Get job seekers by experience level
     * @param experience Experience level
     * @return List of job seekers with specified experience
     */
    public List<UserResponse> getJobSeekersByExperience(String experience) {
        // Only employers can search job seekers
        checkEmployerAccess();
        
        List<User> jobSeekers = userRepository.findJobSeekersByExperience(experience);
        return jobSeekers.stream()
                .map(userMapper::toUserResponse)
                .collect(Collectors.toList());
    }
    
    /**
     * Search users by name
     * @param name Name to search for
     * @return List of users matching the name
     */
    public List<UserResponse> searchUsersByName(String name) {
        List<User> users = userRepository.findByNameContaining(name);
        return users.stream()
                .map(userMapper::toUserResponse)
                .collect(Collectors.toList());
    }
    
    /**
     * Get users by location (city)
     * @param city City to search for
     * @return List of users in the city
     */
    public List<UserResponse> getUsersByLocation(String city) {
        List<User> users = userRepository.findByCityContainingIgnoreCase(city);
        return users.stream()
                .map(userMapper::toUserResponse)
                .collect(Collectors.toList());
    }
    
    // Helper methods
    
    /**
     * Get current authenticated user's email
     * @return Current user's email
     */
    private String getCurrentUserEmail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new RuntimeException("User not authenticated");
        }
        return authentication.getName();
    }
    
    /**
     * Check if current user is an employer
     * Throws exception if not an employer
     */
    private void checkEmployerAccess() {
        String currentUserEmail = getCurrentUserEmail();
        User currentUser = userRepository.findByEmail(currentUserEmail)
                .orElseThrow(() -> UserNotFoundException.byEmail(currentUserEmail));
        
        if (!currentUser.isEmployer()) {
            throw UnauthorizedAccessException.forAction("access job seeker profiles");
        }
    }
    
    /**
     * Get current authenticated user entity
     * @return Current User entity
     */
    public User getCurrentUser() {
        String currentUserEmail = getCurrentUserEmail();
        return userRepository.findByEmail(currentUserEmail)
                .orElseThrow(() -> UserNotFoundException.byEmail(currentUserEmail));
    }
}
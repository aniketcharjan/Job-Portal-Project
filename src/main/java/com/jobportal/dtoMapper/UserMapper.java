package com.jobportal.dtoMapper;

import com.jobportal.model.User;
import com.jobportal.response.UserResponse;
import org.springframework.stereotype.Component;

/**
 * User Mapper
 * This class converts User entity to UserResponse DTO and vice versa
 */
@Component
public class UserMapper {
    
    /**
     * Convert User entity to UserResponse DTO
     * @param user User entity
     * @return UserResponse DTO
     */
    public UserResponse toUserResponse(User user) {
        if (user == null) {
            return null;
        }
        
        UserResponse userResponse = new UserResponse();
        
        // Basic fields
        userResponse.setId(user.getId());
        userResponse.setFirstName(user.getFirstName());
        userResponse.setLastName(user.getLastName());
        userResponse.setEmail(user.getEmail());
        userResponse.setRole(user.getRole());
        userResponse.setPhone(user.getPhone());
        userResponse.setAddress(user.getAddress());
        userResponse.setCity(user.getCity());
        userResponse.setState(user.getState());
        userResponse.setCountry(user.getCountry());
        userResponse.setProfilePicture(user.getProfilePicture());
        
        // Job Seeker specific fields
        userResponse.setResume(user.getResume());
        userResponse.setSkills(user.getSkills());
        userResponse.setExperience(user.getExperience());
        userResponse.setEducation(user.getEducation());
        userResponse.setBio(user.getBio());
        
        // Employer specific fields
        userResponse.setCompanyName(user.getCompanyName());
        userResponse.setCompanyDescription(user.getCompanyDescription());
        userResponse.setWebsite(user.getWebsite());
        userResponse.setCompanySize(user.getCompanySize());
        userResponse.setIndustry(user.getIndustry());
        
        // Timestamps
        userResponse.setCreatedAt(user.getCreatedAt());
        userResponse.setUpdatedAt(user.getUpdatedAt());
        
        return userResponse;
    }
    
    /**
     * Convert UserResponse DTO to User entity
     * Note: This method is used for updates, it doesn't set password or sensitive fields
     * @param userResponse UserResponse DTO
     * @return User entity
     */
    public User toUser(UserResponse userResponse) {
        if (userResponse == null) {
            return null;
        }
        
        User user = new User();
        
        // Basic fields
        user.setId(userResponse.getId());
        user.setFirstName(userResponse.getFirstName());
        user.setLastName(userResponse.getLastName());
        user.setEmail(userResponse.getEmail());
        user.setRole(userResponse.getRole());
        user.setPhone(userResponse.getPhone());
        user.setAddress(userResponse.getAddress());
        user.setCity(userResponse.getCity());
        user.setState(userResponse.getState());
        user.setCountry(userResponse.getCountry());
        user.setProfilePicture(userResponse.getProfilePicture());
        
        // Job Seeker specific fields
        user.setResume(userResponse.getResume());
        user.setSkills(userResponse.getSkills());
        user.setExperience(userResponse.getExperience());
        user.setEducation(userResponse.getEducation());
        user.setBio(userResponse.getBio());
        
        // Employer specific fields
        user.setCompanyName(userResponse.getCompanyName());
        user.setCompanyDescription(userResponse.getCompanyDescription());
        user.setWebsite(userResponse.getWebsite());
        user.setCompanySize(userResponse.getCompanySize());
        user.setIndustry(userResponse.getIndustry());
        
        // Timestamps
        user.setCreatedAt(userResponse.getCreatedAt());
        user.setUpdatedAt(userResponse.getUpdatedAt());
        
        return user;
    }
}
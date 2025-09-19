package com.jobportal.controller;

import com.jobportal.request.UpdateProfileRequest;
import com.jobportal.response.ApiResponse;
import com.jobportal.response.UserResponse;
import com.jobportal.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * User Controller
 * This controller handles user-related endpoints like profile management, user search
 */
@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "http://localhost:3000")
public class UserController {
    
    @Autowired
    private UserService userService;
    
    /**
     * Get user profile by ID
     * GET /api/users/{userId}
     */
    @GetMapping("/{userId}")
    public ResponseEntity<ApiResponse<UserResponse>> getUserProfile(@PathVariable String userId) {
        try {
            UserResponse userResponse = userService.getUserProfile(userId);
            ApiResponse<UserResponse> response = ApiResponse.success("User profile retrieved", userResponse);
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            ApiResponse<UserResponse> response = ApiResponse.error("Failed to get user profile: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    /**
     * Update user profile
     * PUT /api/users/{userId}
     */
    @PutMapping("/{userId}")
    public ResponseEntity<ApiResponse<UserResponse>> updateProfile(
            @PathVariable String userId,
            @Valid @RequestBody UpdateProfileRequest updateRequest) {
        try {
            UserResponse updatedUser = userService.updateProfile(userId, updateRequest);
            ApiResponse<UserResponse> response = ApiResponse.success("Profile updated successfully", updatedUser);
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            ApiResponse<UserResponse> response = ApiResponse.error("Failed to update profile: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    /**
     * Search job seekers by skill (only for employers)
     * GET /api/users/job-seekers/search/skill
     */
    @GetMapping("/job-seekers/search/skill")
    public ResponseEntity<ApiResponse<List<UserResponse>>> searchJobSeekersBySkill(@RequestParam String skill) {
        try {
            List<UserResponse> jobSeekers = userService.searchJobSeekersBySkill(skill);
            ApiResponse<List<UserResponse>> response = ApiResponse.success("Job seekers found", jobSeekers);
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            ApiResponse<List<UserResponse>> response = ApiResponse.error("Search failed: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    /**
     * Get all job seekers (only for employers)
     * GET /api/users/job-seekers
     */
    @GetMapping("/job-seekers")
    public ResponseEntity<ApiResponse<List<UserResponse>>> getAllJobSeekers() {
        try {
            List<UserResponse> jobSeekers = userService.getAllJobSeekers();
            ApiResponse<List<UserResponse>> response = ApiResponse.success("Job seekers retrieved", jobSeekers);
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            ApiResponse<List<UserResponse>> response = ApiResponse.error("Failed to get job seekers: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    /**
     * Search job seekers by experience level (only for employers)
     * GET /api/users/job-seekers/search/experience
     */
    @GetMapping("/job-seekers/search/experience")
    public ResponseEntity<ApiResponse<List<UserResponse>>> getJobSeekersByExperience(@RequestParam String experience) {
        try {
            List<UserResponse> jobSeekers = userService.getJobSeekersByExperience(experience);
            ApiResponse<List<UserResponse>> response = ApiResponse.success("Job seekers found", jobSeekers);
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            ApiResponse<List<UserResponse>> response = ApiResponse.error("Search failed: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    /**
     * Search users by name
     * GET /api/users/search/name
     */
    @GetMapping("/search/name")
    public ResponseEntity<ApiResponse<List<UserResponse>>> searchUsersByName(@RequestParam String name) {
        try {
            List<UserResponse> users = userService.searchUsersByName(name);
            ApiResponse<List<UserResponse>> response = ApiResponse.success("Users found", users);
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            ApiResponse<List<UserResponse>> response = ApiResponse.error("Search failed: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    /**
     * Get users by location
     * GET /api/users/search/location
     */
    @GetMapping("/search/location")
    public ResponseEntity<ApiResponse<List<UserResponse>>> getUsersByLocation(@RequestParam String city) {
        try {
            List<UserResponse> users = userService.getUsersByLocation(city);
            ApiResponse<List<UserResponse>> response = ApiResponse.success("Users found", users);
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            ApiResponse<List<UserResponse>> response = ApiResponse.error("Search failed: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
}
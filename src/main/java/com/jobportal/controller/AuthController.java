package com.jobportal.controller;

import com.jobportal.request.LoginRequest;
import com.jobportal.request.SignupRequest;
import com.jobportal.response.ApiResponse;
import com.jobportal.response.AuthResponse;
import com.jobportal.response.UserResponse;
import com.jobportal.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Authentication Controller
 * This controller handles authentication related endpoints like login, signup
 */
@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:3000")
public class AuthController {
    
    @Autowired
    private AuthService authService;
    
    /**
     * User Signup/Registration endpoint
     * POST /api/auth/signup
     */
    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<AuthResponse>> signup(@Valid @RequestBody SignupRequest signupRequest) {
        try {
            AuthResponse authResponse = authService.signup(signupRequest);
            ApiResponse<AuthResponse> response = ApiResponse.success("User registered successfully", authResponse);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
            
        } catch (Exception e) {
            ApiResponse<AuthResponse> response = ApiResponse.error("Registration failed: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
    
    /**
     * User Login endpoint
     * POST /api/auth/login
     */
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> login(@Valid @RequestBody LoginRequest loginRequest) {
    	
    	System.out.println("Login attempt for user: --------------------------");
        try {
            AuthResponse authResponse = authService.login(loginRequest);
            ApiResponse<AuthResponse> response = ApiResponse.success("Login successful", authResponse);
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            ApiResponse<AuthResponse> response = ApiResponse.error("Login failed: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
    }
    
    /**
     * Get current user profile endpoint
     * GET /api/auth/me
     */
    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UserResponse>> getCurrentUser() {
        try {
            UserResponse userResponse = authService.getCurrentUser();
            ApiResponse<UserResponse> response = ApiResponse.success("User profile retrieved", userResponse);
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            ApiResponse<UserResponse> response = ApiResponse.error("Failed to get user profile: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
    }
    
    /**
     * Test endpoint to check if server is running
     * GET /api/auth/test
     */
    @GetMapping("/test")
    public ResponseEntity<ApiResponse<String>> test() {
        ApiResponse<String> response = ApiResponse.success("Job Portal API is running successfully!");
        return ResponseEntity.ok(response);
    }
}
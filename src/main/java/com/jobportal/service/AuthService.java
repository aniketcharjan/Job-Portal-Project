package com.jobportal.service;

import com.jobportal.config.JwtProvider;
import com.jobportal.exception.UserAlreadyExistsException;
import com.jobportal.exception.UserNotFoundException;
import com.jobportal.model.User;
import com.jobportal.repository.UserRepository;
import com.jobportal.request.LoginRequest;
import com.jobportal.request.SignupRequest;
import com.jobportal.response.AuthResponse;
import com.jobportal.response.UserResponse;
import com.jobportal.dtoMapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Authentication Service
 * This service handles user authentication operations like login, signup, etc.
 */
@Service
public class AuthService {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private CustomUserDetailsService customUserDetailsService;
    
    @Autowired
    private JwtProvider jwtProvider;
    
    @Autowired
    private UserMapper userMapper;
    
    /**
     * User Signup/Registration
     * @param signupRequest Registration details
     * @return AuthResponse with JWT token and user details
     */
    public AuthResponse signup(SignupRequest signupRequest) {
        // Check if user already exists
        if (userRepository.existsByEmail(signupRequest.getEmail())) {
            throw UserAlreadyExistsException.withEmail(signupRequest.getEmail());
        }
        
        // Validate role
        String role = signupRequest.getRole().toUpperCase();
        if (!role.equals("JOB_SEEKER") && !role.equals("EMPLOYER")) {
            throw new IllegalArgumentException("Invalid role. Must be JOB_SEEKER or EMPLOYER");
        }
        
        // Create new user
        User user = new User();
        user.setFirstName(signupRequest.getFirstName());
        user.setLastName(signupRequest.getLastName());
        user.setEmail(signupRequest.getEmail().toLowerCase());
        user.setPassword(passwordEncoder.encode(signupRequest.getPassword())); // Encode password
        user.setRole(role);
        user.setPhone(signupRequest.getPhone());
        
        // Set company name for employers
        if (role.equals("EMPLOYER") && signupRequest.getCompanyName() != null) {
            user.setCompanyName(signupRequest.getCompanyName());
        }
        
        // Save user to database
        User savedUser = userRepository.save(user);
        
        // Create authentication token
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                savedUser.getEmail(), 
                signupRequest.getPassword()
        );
        
        // Generate JWT token
        String jwt = jwtProvider.generateTokenFromEmail(savedUser.getEmail());
        
        // Convert to response DTO
        UserResponse userResponse = userMapper.toUserResponse(savedUser);
        
        return new AuthResponse(jwt, "User registered successfully", userResponse);
    }
    
    /**
     * User Login
     * @param loginRequest Login credentials
     * @return AuthResponse with JWT token and user details
     */
    public AuthResponse login(LoginRequest loginRequest) {
        String email = loginRequest.getEmail().toLowerCase();
        
        // Load user details
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(email);
        
        // Check password
        if (!passwordEncoder.matches(loginRequest.getPassword(), userDetails.getPassword())) {
            throw new BadCredentialsException("Invalid email or password");
        }
        
        // Create authentication
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                userDetails, 
                null, 
                userDetails.getAuthorities()
        );
        
        // Set authentication in security context
        SecurityContextHolder.getContext().setAuthentication(authentication);
        
        // Generate JWT token
        String jwt = jwtProvider.generateToken(authentication);
        
        // Get user details
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> UserNotFoundException.byEmail(email));
        
        // Convert to response DTO
        UserResponse userResponse = userMapper.toUserResponse(user);
        
        return new AuthResponse(jwt, "Login successful", userResponse);
    }
    
    /**
     * Get current authenticated user
     * @return UserResponse of current user
     */
    public UserResponse getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new RuntimeException("User not authenticated");
        }
        
        String email = authentication.getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> UserNotFoundException.byEmail(email));
        
        return userMapper.toUserResponse(user);
    }
    
    /**
     * Get user entity by email (for internal use)
     * @param email User email
     * @return User entity
     */
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> UserNotFoundException.byEmail(email));
    }
}
package com.jobportal.service;

import com.jobportal.model.User;
import com.jobportal.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Custom User Details Service
 * This service is used by Spring Security to load user details for authentication
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {
    
    @Autowired
    private UserRepository userRepository;
    
    /**
     * Load user by username (email in our case)
     * This method is called by Spring Security during authentication
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // Find user by email
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
        
        // Create authorities (roles) for the user
        List<GrantedAuthority> authorities = new ArrayList<>();
        
        // Add role as authority (Spring Security expects roles to have "ROLE_" prefix)
        authorities.add(new SimpleGrantedAuthority("ROLE_" + user.getRole()));
        
        // Return UserDetails object that Spring Security can use
        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getEmail()) // Email as username
                .password(user.getPassword()) // Encoded password
                .authorities(authorities) // User roles/authorities
                .build();
    }
}
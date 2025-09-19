package com.jobportal.config;

import com.jobportal.service.CustomUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * JWT Token Validation Filter
 * This filter intercepts every HTTP request and validates JWT tokens
 */
@Component
public class JwtTokenValidation extends OncePerRequestFilter {

    @Autowired
    private JwtProvider jwtProvider;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    /**
     * Filter method that validates JWT tokens for each request
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, 
                                  HttpServletResponse response, 
                                  FilterChain filterChain) throws ServletException, IOException {
        
        try {
            // Extract Authorization header
            String authHeader = request.getHeader(JwtConstant.JWT_HEADER);
            
            // Extract JWT token from header
            String jwt = jwtProvider.extractTokenFromHeader(authHeader);
            
            // If token exists and is valid
            if (jwt != null && jwtProvider.validateJwtToken(jwt)) {
                
                // Extract email from token
                String email = jwtProvider.getEmailFromJwtToken(jwt);
                
                // Load user details
                UserDetails userDetails = customUserDetailsService.loadUserByUsername(email);
                
                // Create authentication token
                UsernamePasswordAuthenticationToken authentication = 
                        new UsernamePasswordAuthenticationToken(
                                userDetails, 
                                null, 
                                userDetails.getAuthorities()
                        );
                
                // Set authentication details
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                
                // Set authentication in security context
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
            
        } catch (Exception e) {
            System.err.println("Cannot set user authentication: " + e.getMessage());
        }
        
        // Continue filter chain
        filterChain.doFilter(request, response);
    }
}
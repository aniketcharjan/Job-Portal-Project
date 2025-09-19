package com.jobportal.config;

import com.jobportal.service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfigurationSource;

/**
 * Security Configuration
 * This class configures Spring Security for the application
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    
    @Autowired
    private CustomUserDetailsService customUserDetailsService;
    
    @Autowired
    private JwtTokenValidation jwtTokenValidation;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private CorsConfigurationSource corsConfigurationSource;
    
    /**
     * Configure security filter chain
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            // Disable CSRF for REST API
            .csrf(csrf -> csrf.disable())
            
            // Enable CORS
            .cors(cors -> cors.configurationSource(corsConfigurationSource))
            
            // Configure session management (stateless for JWT)
            .sessionManagement(session -> 
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )
            
            // Configure authorization rules
            .authorizeHttpRequests(authz -> authz
                // Public endpoints (no authentication required)
                .requestMatchers("/api/auth/**").permitAll()
                .requestMatchers("/api/jobs/public/**").permitAll()
                .requestMatchers("/api/public/**").permitAll()
                
                // Job seeker specific endpoints
                .requestMatchers("/api/applications/apply").hasRole("JOB_SEEKER")
                .requestMatchers("/api/applications/my-applications").hasRole("JOB_SEEKER")
                .requestMatchers("/api/applications/stats").hasRole("JOB_SEEKER")
                
                // Employer specific endpoints
                .requestMatchers("/api/jobs/create").hasRole("EMPLOYER")
                .requestMatchers("/api/jobs/my-jobs").hasRole("EMPLOYER")
                .requestMatchers("/api/applications/job/**").hasRole("EMPLOYER")
                .requestMatchers("/api/applications/update-status").hasRole("EMPLOYER")
                
                // Protected endpoints (require authentication)
                .requestMatchers("/api/users/**").authenticated()
                .requestMatchers("/api/jobs/**").authenticated()
                .requestMatchers("/api/applications/**").authenticated()
                
                // All other requests require authentication
                .anyRequest().authenticated()
            )
            
            // Add JWT token filter before UsernamePasswordAuthenticationFilter
            .addFilterBefore(jwtTokenValidation, UsernamePasswordAuthenticationFilter.class)
            
            // Configure authentication provider
            .authenticationProvider(authenticationProvider());
        
        return http.build();
    }
    
    /**
     * Authentication provider configuration
     */
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(customUserDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder);
        return authProvider;
    }
    
    /**
     * Authentication manager bean
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }
}
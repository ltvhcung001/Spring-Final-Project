package org.example.internmanagement.config;

import org.example.internmanagement.security.CustomUserDetailsService;
import org.example.internmanagement.security.JwtAccessDeniedHandler;
import org.example.internmanagement.security.JwtAuthenticationEntryPoint;
import org.example.internmanagement.security.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Autowired
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @Autowired
    private JwtAccessDeniedHandler jwtAccessDeniedHandler;

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .exceptionHandling(handling -> handling
                    .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                    .accessDeniedHandler(jwtAccessDeniedHandler))
                .sessionManagement(session -> session
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authz -> authz
                        // Auth
                        .requestMatchers(HttpMethod.POST, "/api/auth/login").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/auth/me").hasAnyRole("ADMIN", "MENTOR", "STUDENT")
                        
                        // Users
                        .requestMatchers("/api/users/**").hasRole("ADMIN")
                        
                        // Students
                        .requestMatchers(HttpMethod.GET, "/api/students").hasAnyRole("ADMIN", "MENTOR")
                        .requestMatchers(HttpMethod.POST, "/api/students").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/students/*").hasAnyRole("ADMIN", "MENTOR", "STUDENT")
                        .requestMatchers(HttpMethod.PUT, "/api/students/*").hasAnyRole("ADMIN", "STUDENT")
                        
                        // Mentors
                        .requestMatchers(HttpMethod.GET, "/api/mentors").hasAnyRole("ADMIN", "STUDENT")
                        .requestMatchers(HttpMethod.POST, "/api/mentors").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/mentors/*").hasAnyRole("ADMIN", "MENTOR", "STUDENT")
                        .requestMatchers(HttpMethod.PUT, "/api/mentors/*").hasAnyRole("ADMIN", "MENTOR")
                        
                        // Internship Phases
                        .requestMatchers(HttpMethod.GET, "/api/internship_phases", "/api/internship_phases/*").hasAnyRole("ADMIN", "MENTOR", "STUDENT")
                        .requestMatchers(HttpMethod.POST, "/api/internship_phases").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/internship_phases/*").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/internship_phases/*").hasRole("ADMIN")
                        
                        // Evaluation Criteria
                        .requestMatchers(HttpMethod.GET, "/api/evaluation_criteria", "/api/evaluation_criteria/*").hasAnyRole("ADMIN", "MENTOR", "STUDENT")
                        .requestMatchers(HttpMethod.POST, "/api/evaluation_criteria").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/evaluation_criteria/*").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/evaluation_criteria/*").hasRole("ADMIN")

                        // Assessment Rounds
                        .requestMatchers(HttpMethod.GET, "/api/assessment_rounds", "/api/assessment_rounds/*").hasAnyRole("ADMIN", "MENTOR", "STUDENT")
                        .requestMatchers(HttpMethod.POST, "/api/assessment_rounds").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/assessment_rounds/*").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/assessment_rounds/*").hasRole("ADMIN")

                        // Round Criteria
                        .requestMatchers(HttpMethod.GET, "/api/round_criteria", "/api/round_criteria/*").hasAnyRole("ADMIN", "MENTOR", "STUDENT")
                        .requestMatchers(HttpMethod.POST, "/api/round_criteria").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/round_criteria/*").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/round_criteria/*").hasRole("ADMIN")

                        // Internship Assignments
                        .requestMatchers(HttpMethod.GET, "/api/internship_assignments", "/api/internship_assignments/*").hasAnyRole("ADMIN", "MENTOR", "STUDENT")
                        .requestMatchers(HttpMethod.POST, "/api/internship_assignments").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/internship_assignments/*/status").hasRole("ADMIN")

                        // Assessment Results
                        .requestMatchers(HttpMethod.GET, "/api/assessment_results", "/api/assessment_results/*").hasAnyRole("ADMIN", "MENTOR", "STUDENT")
                        .requestMatchers(HttpMethod.POST, "/api/assessment_results").hasRole("MENTOR")
                        .requestMatchers(HttpMethod.PUT, "/api/assessment_results/*").hasRole("MENTOR")
                        
                        // Public endpoints
                        .requestMatchers("/api/public/**").permitAll()
                        .requestMatchers("/actuator/**").permitAll()
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()
                        
                        .anyRequest().authenticated())
                .authenticationProvider(authenticationProvider());

        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}




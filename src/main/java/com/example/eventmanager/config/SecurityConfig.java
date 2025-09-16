package com.example.eventmanager.config;

import com.example.eventmanager.service.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final UserService userService;

    public SecurityConfig(UserService userService) {
        this.userService = userService;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                // Allow access to public pages without authentication
                .requestMatchers("/", "/home", "/register", "/login", "/css/**", "/js/**", "/images/**", "/favicon.ico").permitAll()
                // All other requests require authentication
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/login")                    // Custom login page
                .loginProcessingUrl("/perform_login")   // URL that processes the login form
                .defaultSuccessUrl("/dashboard", true)  // Redirect after successful login
                .failureUrl("/login?error=true")        // Redirect after failed login
                .usernameParameter("username")          // Name of username field in form
                .passwordParameter("password")          // Name of password field in form
                .permitAll()
            )
            .logout(logout -> logout
                .logoutUrl("/logout")                   // URL to trigger logout
                .logoutSuccessUrl("/login?logout=true") // Redirect after logout
                .invalidateHttpSession(true)           // Invalidate session
                .deleteCookies("JSESSIONID")           // Delete session cookie
                .permitAll()
            )
            // Remove session management for now to fix the lazy loading issue
            ;

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
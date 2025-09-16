package com.example.eventmanager.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import com.example.eventmanager.model.User;
import com.example.eventmanager.service.UserService;

@Component
public class DataInitializer implements CommandLineRunner {
    
    private final UserService userService;
    
    public DataInitializer(@Lazy UserService userService) {
        this.userService = userService;
    }
    
    @Override
    public void run(String... args) throws Exception {
        // Create default admin user if not exists
        if (!userService.existsByUsername("admin")) {
            User admin = new User();
            admin.setFirstName("Admin");
            admin.setLastName("User");
            admin.setUsername("admin");
            admin.setEmail("admin@eventmanagement.com");
            admin.setPassword("admin123");
            admin.setPhone("1234567890");
            admin.setRole(User.Role.ADMIN);
            
            userService.createUser(admin);
            System.out.println("Default admin user created:");
            System.out.println("Username: admin");
            System.out.println("Password: admin123");
        }
        
        // Create a sample regular user
        if (!userService.existsByUsername("user")) {
            User user = new User();
            user.setFirstName("John");
            user.setLastName("Doe");
            user.setUsername("user");
            user.setEmail("user@eventmanagement.com");
            user.setPassword("user123");
            user.setPhone("9876543210");
            user.setRole(User.Role.USER);
            
            userService.createUser(user);
            System.out.println("Sample user created:");
            System.out.println("Username: user");
            System.out.println("Password: user123");
        }
    }
}
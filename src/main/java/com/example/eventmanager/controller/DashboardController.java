package com.example.eventmanager.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.eventmanager.service.EventService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class DashboardController {

    private final EventService eventService;

    @GetMapping("/dashboard")
    public String dashboard(Authentication authentication, Model model) {
        // Add user information to the model
        model.addAttribute("username", authentication.getName());
        
        // Check if user is admin and redirect to appropriate dashboard
        if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
            return "redirect:/admin/dashboard";
        } else {
            model.addAttribute("role", "User");
            // Add upcoming events for users to see and register
            model.addAttribute("upcomingEvents", eventService.getUpcomingEvents());
            return "dashboard/user-dashboard";
        }
    }

    @GetMapping("/user/dashboard")
    public String userDashboard(Authentication authentication, Model model) {
        model.addAttribute("username", authentication.getName());
        model.addAttribute("role", "User");
        // Add upcoming events for users to see and register
        model.addAttribute("upcomingEvents", eventService.getUpcomingEvents());
        return "dashboard/user-dashboard";
    }
}
package com.example.eventmanager.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.eventmanager.model.Event;
import com.example.eventmanager.model.User;
import com.example.eventmanager.service.EventService;
import com.example.eventmanager.service.UserService;

import jakarta.validation.Valid;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class HomeController {
    
    private final EventService eventService;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    
    @GetMapping("/")
    public String home(Model model) {
        List<Event> upcomingEvents = eventService.getUpcomingEvents();
        model.addAttribute("upcomingEvents", upcomingEvents.size() > 6 ? upcomingEvents.subList(0, 6) : upcomingEvents);
        return "index";
    }
    
    @GetMapping("/login")
    public String login() {
        return "auth/login";
    }
    
    @GetMapping("/register")
    public String register(Model model) {
        model.addAttribute("user", new User());
        return "auth/register";
    }
    
    @PostMapping("/register")
    public String registerUser(@Valid @ModelAttribute("user") User user, 
                              BindingResult result, 
                              RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "auth/register";
        }
        
        try {
            userService.registerUser(user);
            redirectAttributes.addFlashAttribute("success", "Registration successful! Please login.");
            return "redirect:/login";
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/register";
        }
    }
}

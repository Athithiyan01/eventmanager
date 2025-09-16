package com.example.eventmanager.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.eventmanager.model.Event;
import com.example.eventmanager.model.User;
import com.example.eventmanager.service.EventRegistrationService;
import com.example.eventmanager.service.EventService;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/events")
@RequiredArgsConstructor
public class EventController {
    
    private final EventService eventService;
    private final EventRegistrationService registrationService;
    
    @GetMapping
    public String listEvents(Model model, @RequestParam(required = false) String search) {
        List<Event> events;
        if (search != null && !search.trim().isEmpty()) {
            events = eventService.searchEvents(search);
        } else {
            events = eventService.getUpcomingEvents();
        }
        
        model.addAttribute("events", events);
        model.addAttribute("search", search);
        return "events/list";
    }
    
    @GetMapping("/{id}")
    public String viewEvent(@PathVariable Long id, 
                           @AuthenticationPrincipal User user, 
                           Model model) {
        Optional<Event> eventOpt = eventService.getEventById(id);
        if (eventOpt.isEmpty()) {
            return "redirect:/events";
        }
        
        Event event = eventOpt.get();
        boolean isRegistered = registrationService.isUserRegistered(user, event);
        
        model.addAttribute("event", event);
        model.addAttribute("isRegistered", isRegistered);
        model.addAttribute("registrationCount", registrationService.getRegistrationCount(event));
        
        return "events/detail";
    }
    
    @PostMapping("/{id}/register")
    public String registerForEvent(@PathVariable Long id, 
                                  @AuthenticationPrincipal User user, 
                                  RedirectAttributes redirectAttributes) {
        Optional<Event> eventOpt = eventService.getEventById(id);
        if (eventOpt.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Event not found");
            return "redirect:/events";
        }
        
        try {
            registrationService.registerForEvent(user, eventOpt.get());
            redirectAttributes.addFlashAttribute("success", "Successfully registered for the event!");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        
        return "redirect:/events/" + id;
    }
    
    @PostMapping("/{id}/cancel")
    public String cancelRegistration(@PathVariable Long id, 
                                   @AuthenticationPrincipal User user, 
                                   RedirectAttributes redirectAttributes) {
        Optional<Event> eventOpt = eventService.getEventById(id);
        if (eventOpt.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Event not found");
            return "redirect:/events";
        }
        
        registrationService.cancelRegistration(user, eventOpt.get());
        redirectAttributes.addFlashAttribute("success", "Registration cancelled successfully!");
        
        return "redirect:/events/" + id;
    }
}
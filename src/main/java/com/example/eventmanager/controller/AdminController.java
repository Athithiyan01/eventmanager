package com.example.eventmanager.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.eventmanager.model.Event;
import com.example.eventmanager.model.EventRegistration;
import com.example.eventmanager.model.User;
import com.example.eventmanager.service.EventRegistrationService;
import com.example.eventmanager.service.EventService;
import com.example.eventmanager.service.UserService;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
public class AdminController {

    private final EventService eventService;
    private final UserService userService;
    private final EventRegistrationService registrationService;

    // ================== DASHBOARD ==================
    @GetMapping("/dashboard")
    public String adminDashboard(Model model) {
        List<Event> allEvents = eventService.getAllEvents();
        List<User> allUsers = userService.getAllUsers();

        model.addAttribute("totalEvents", allEvents.size());
        model.addAttribute("totalUsers", allUsers.size());
        model.addAttribute("upcomingEvents", eventService.getUpcomingEvents().size());
        model.addAttribute("pastEvents", eventService.getPastEvents().size());

        return "admin/dashboard";
    }

    // ================== EVENTS ==================
    @GetMapping("/events")
    public String manageEvents(Model model) {
        model.addAttribute("events", eventService.getAllEvents());
        return "admin/events/list";
    }

    @GetMapping("/events/create")
    public String createEventForm(Model model) {
        model.addAttribute("event", new Event());
        model.addAttribute("categories", Event.EventCategory.values());
        return "admin/events/form";
    }

    @PostMapping("/events/create")
    public String createEvent(@Valid @ModelAttribute("event") Event event,
                              BindingResult result,
                              Model model,
                              RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("categories", Event.EventCategory.values());
            return "admin/events/form";
        }

        eventService.createEvent(event);
        redirectAttributes.addFlashAttribute("success", "Event created successfully!");
        return "redirect:/admin/events";
    }

    @GetMapping("/events/{id}/edit")
    public String editEventForm(@PathVariable Long id, Model model) {
        Optional<Event> eventOpt = eventService.getEventById(id);
        if (eventOpt.isEmpty()) {
            return "redirect:/admin/events";
        }

        model.addAttribute("event", eventOpt.get());
        model.addAttribute("categories", Event.EventCategory.values());
        return "admin/events/form";
    }

    @PostMapping("/events/{id}/edit")
    public String updateEvent(@PathVariable Long id,
                              @Valid @ModelAttribute("event") Event event,
                              BindingResult result,
                              Model model,
                              RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("categories", Event.EventCategory.values());
            return "admin/events/form";
        }

        event.setId(id);
        eventService.updateEvent(event);
        redirectAttributes.addFlashAttribute("success", "Event updated successfully!");
        return "redirect:/admin/events";
    }

    @PostMapping("/events/{id}/delete")
    public String deleteEvent(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        eventService.deleteEvent(id);
        redirectAttributes.addFlashAttribute("success", "Event deleted successfully!");
        return "redirect:/admin/events";
    }

    @GetMapping("/events/{id}/registrations")
    public String viewEventRegistrations(@PathVariable Long id, Model model) {
        Optional<Event> eventOpt = eventService.getEventById(id);
        if (eventOpt.isEmpty()) {
            return "redirect:/admin/events";
        }

        Event event = eventOpt.get();
        List<EventRegistration> registrations = registrationService.getEventRegistrations(event);

        model.addAttribute("event", event);
        model.addAttribute("registrations", registrations);

        return "admin/events/registrations";
    }

    @PostMapping("/registrations/{id}/attendance")
    public String markAttendance(@PathVariable Long id,
                                 @RequestParam boolean attended,
                                 @RequestParam Long eventId,
                                 RedirectAttributes redirectAttributes) {
        registrationService.markAttendance(id, attended);
        redirectAttributes.addFlashAttribute("success", "Attendance updated successfully!");
        return "redirect:/admin/events/" + eventId + "/registrations";
    }

    // ================== USERS ==================
    @GetMapping("/users")
    public String manageUsers(Model model) {
        model.addAttribute("users", userService.getAllUsers());
        return "admin/users/list"; // ✅ matches list.html
    }

    @GetMapping("/users/create")
    public String createUserForm(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("roles", User.Role.values());
        return "admin/users/create"; // ✅ matches create.html
    }

    @PostMapping("/users/save")
    public String saveUser(@Valid @ModelAttribute("user") User user,
                           BindingResult result,
                           Model model,
                           RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("roles", User.Role.values());
            return "admin/users/create";
        }

        try {
            userService.createUser(user);
            redirectAttributes.addFlashAttribute("success", "User created successfully!");
            return "redirect:/admin/users";
        } catch (Exception e) {
            model.addAttribute("roles", User.Role.values());
            model.addAttribute("error", "Error creating user: " + e.getMessage());
            return "admin/users/create";
        }
    }

    @GetMapping("/users/edit/{id}")
    public String editUserForm(@PathVariable Long id, Model model) {
        Optional<User> userOpt = userService.getUserById(id);
        if (userOpt.isEmpty()) {
            return "redirect:/admin/users";
        }

        model.addAttribute("user", userOpt.get());
        model.addAttribute("roles", User.Role.values());
        return "admin/users/edit"; // ✅ matches edit.html
    }

    @PostMapping("/users/update")
    public String updateUser(@Valid @ModelAttribute("user") User user,
                             BindingResult result,
                             Model model,
                             RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("roles", User.Role.values());
            return "admin/users/edit";
        }

        try {
            userService.updateUser(user);
            redirectAttributes.addFlashAttribute("success", "User updated successfully!");
            return "redirect:/admin/users";
        } catch (Exception e) {
            model.addAttribute("roles", User.Role.values());
            model.addAttribute("error", "Error updating user: " + e.getMessage());
            return "admin/users/edit";
        }
    }

    @GetMapping("/users/delete/{id}")
    public String deleteUser(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        userService.deleteUser(id);
        redirectAttributes.addFlashAttribute("success", "User deleted successfully!");
        return "redirect:/admin/users";
    }
}

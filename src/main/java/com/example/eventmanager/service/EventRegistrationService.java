package com.example.eventmanager.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import com.example.eventmanager.model.Event;
import com.example.eventmanager.model.EventRegistration;
import com.example.eventmanager.model.User;
import com.example.eventmanager.repository.EventRegistrationRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EventRegistrationService {
    
    private final EventRegistrationRepository registrationRepository;
    private final EventService eventService;
    private final EmailService emailService;
    
    /**
     * Registers a user for an event with validations
     */
    public EventRegistration registerForEvent(User user, Event event) {
        // check if already registered
        if (registrationRepository.existsByUserAndEvent(user, event)) {
            throw new IllegalStateException("You are already registered for this event");
        }

        // check seat availability
        if (!event.hasAvailableSeats()) {
            throw new IllegalStateException("No available seats for this event");
        }

        // check deadline
        if (event.getRegistrationDeadline() != null && LocalDateTime.now().isAfter(event.getRegistrationDeadline())) {
            throw new IllegalStateException("Registration deadline has passed");
        }

        // create registration
        EventRegistration registration = new EventRegistration();
        registration.setUser(user);
        registration.setEvent(event);
        registration.setRegistrationDate(LocalDateTime.now());
        registration.setStatus(EventRegistration.RegistrationStatus.REGISTERED);
        registration.setAttended(false);

        EventRegistration savedRegistration = registrationRepository.save(registration);

        // decrease available seats
        eventService.decreaseAvailableSeats(event);

        // send confirmation email
        try {
            emailService.sendRegistrationConfirmation(user, event);
        } catch (Exception e) {
            // log error but don’t block registration
            System.err.println("Failed to send confirmation email: " + e.getMessage());
        }

        return savedRegistration;
    }

    /**
     * Cancel a user’s registration
     */
    public void cancelRegistration(User user, Event event) {
        Optional<EventRegistration> registration = registrationRepository.findByUserAndEvent(user, event);
        if (registration.isPresent()) {
            registrationRepository.delete(registration.get());
            eventService.increaseAvailableSeats(event);
        }
    }

    public List<EventRegistration> getUserRegistrations(User user) {
        return registrationRepository.findByUser(user);
    }

    public List<EventRegistration> getEventRegistrations(Event event) {
        return registrationRepository.findByEvent(event);
    }

    public Optional<EventRegistration> getRegistration(User user, Event event) {
        return registrationRepository.findByUserAndEvent(user, event);
    }

    public boolean isUserRegistered(User user, Event event) {
        return registrationRepository.existsByUserAndEvent(user, event);
    }

    public void markAttendance(Long registrationId, boolean attended) {
        registrationRepository.findById(registrationId).ifPresent(reg -> {
            reg.setAttended(attended);
            registrationRepository.save(reg);
        });
    }

    public List<EventRegistration> getAttendees(Event event) {
        return registrationRepository.findAttendeesByEvent(event);
    }

    public Long getRegistrationCount(Event event) {
        return registrationRepository.countByEvent(event);
    }
}

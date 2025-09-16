package com.example.eventmanager.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.example.eventmanager.model.Event;
import com.example.eventmanager.model.User;

import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {
    
    private final JavaMailSender mailSender;
    
    @Async
    public void sendRegistrationConfirmation(User user, Event event) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(user.getEmail());
            message.setSubject("Event Registration Confirmation - " + event.getName());
            message.setText(buildRegistrationConfirmationText(user, event));
            
            mailSender.send(message);
            log.info("Registration confirmation email sent to: {}", user.getEmail());
        } catch (Exception e) {
            log.error("Failed to send registration confirmation email", e);
        }
    }
    
    @Async
    public void sendEventReminder(User user, Event event) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(user.getEmail());
            message.setSubject("Event Reminder - " + event.getName());
            message.setText(buildEventReminderText(user, event));
            
            mailSender.send(message);
            log.info("Event reminder email sent to: {}", user.getEmail());
        } catch (Exception e) {
            log.error("Failed to send event reminder email", e);
        }
    }
    
    private String buildRegistrationConfirmationText(User user, Event event) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy 'at' hh:mm a");
        
        return String.format(
            "Dear %s,\n\n" +
            "Thank you for registering for the event: %s\n\n" +
            "Event Details:\n" +
            "Date: %s\n" +
            "Location: %s\n" +
            "Speaker: %s\n\n" +
            "We look forward to seeing you at the event!\n\n" +
            "Best regards,\n" +
            "Event Management Team",
            user.getFullName(),
            event.getName(),
            event.getEventDate().format(formatter),
            event.getLocation(),
            event.getSpeaker() != null ? event.getSpeaker() : "TBA"
        );
    }
    
    private String buildEventReminderText(User user, Event event) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy 'at' hh:mm a");
        
        return String.format(
            "Dear %s,\n\n" +
            "This is a reminder about the upcoming event: %s\n\n" +
            "Event Details:\n" +
            "Date: %s\n" +
            "Location: %s\n" +
            "Speaker: %s\n\n" +
            "Don't forget to attend!\n\n" +
            "Best regards,\n" +
            "Event Management Team",
            user.getFullName(),
            event.getName(),
            event.getEventDate().format(formatter),
            event.getLocation(),
            event.getSpeaker() != null ? event.getSpeaker() : "TBA"
        );
    }
}
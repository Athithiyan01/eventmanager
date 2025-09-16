package com.example.eventmanager.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "event_registrations", 
       uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "event_id"}))
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventRegistration {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id", nullable = false)
    private Event event;
    
    @Column(name = "registration_date")
    private LocalDateTime registrationDate = LocalDateTime.now();
    
    @Enumerated(EnumType.STRING)
    private RegistrationStatus status = RegistrationStatus.REGISTERED;
    
    @Column(name = "attended")
    private Boolean attended = false;
    
    private String notes;
    
    public enum RegistrationStatus {
        REGISTERED, CANCELLED, CONFIRMED
    }
}
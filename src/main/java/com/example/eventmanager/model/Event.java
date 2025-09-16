package com.example.eventmanager.model;


import jakarta.persistence.*;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "events")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Event {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "Event name is required")
    private String name;
    
    @NotBlank(message = "Description is required")
    @Column(columnDefinition = "TEXT")
    private String description;
    
@NotNull(message = "Event date is required")
@Future(message = "Event date must be in the future")
@Column(name = "event_date")
@DateTimeFormat(pattern = "yyyy-MM-dd")  // matches HTML <input type="date">
private LocalDate eventDate;

    
    @NotBlank(message = "Location is required")
    private String location;
    
    @Positive(message = "Capacity must be positive")
    private Integer capacity;
    
    @Column(name = "available_seats")
    private Integer availableSeats;
    
    @Enumerated(EnumType.STRING)
    private EventCategory category;
    
    private String speaker;
    
    @Column(name = "registration_deadline")
    private LocalDateTime registrationDeadline;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt = LocalDateTime.now();
    
    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<EventRegistration> registrations;
    
    public enum EventCategory {
        TECHNOLOGY, BUSINESS, EDUCATION, HEALTH, ENTERTAINMENT, SPORTS, OTHER
    }
    
    public boolean hasAvailableSeats() {
        return availableSeats != null && availableSeats > 0;
    }
    
    public int getRegisteredCount() {
        return registrations != null ? registrations.size() : 0;
    }
}
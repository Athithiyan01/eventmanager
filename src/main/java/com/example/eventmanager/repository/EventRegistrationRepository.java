package com.example.eventmanager.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.eventmanager.model.Event;
import com.example.eventmanager.model.EventRegistration;
import com.example.eventmanager.model.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface EventRegistrationRepository extends JpaRepository<EventRegistration, Long> {
    List<EventRegistration> findByUser(User user);
    List<EventRegistration> findByEvent(Event event);
    Optional<EventRegistration> findByUserAndEvent(User user, Event event);
    boolean existsByUserAndEvent(User user, Event event);
    
    @Query("SELECT COUNT(r) FROM EventRegistration r WHERE r.event = :event")
    Long countByEvent(@Param("event") Event event);
    
    @Query("SELECT r FROM EventRegistration r WHERE r.event = :event AND r.attended = true")
    List<EventRegistration> findAttendeesByEvent(@Param("event") Event event);
}
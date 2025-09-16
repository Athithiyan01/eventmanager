package com.example.eventmanager.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import com.example.eventmanager.model.Event;
import com.example.eventmanager.repository.EventRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EventService {
    
    private final EventRepository eventRepository;
    
    public Event createEvent(Event event) {
        event.setAvailableSeats(event.getCapacity());
        event.setCreatedAt(LocalDateTime.now());
        event.setUpdatedAt(LocalDateTime.now());
        return eventRepository.save(event);
    }
    
    public List<Event> getAllEvents() {
        return eventRepository.findAll();
    }
    
    public List<Event> getUpcomingEvents() {
        return eventRepository.findByEventDateAfterOrderByEventDateAsc(LocalDate.now());
    }
    
    public List<Event> getPastEvents() {
        return eventRepository.findByEventDateBeforeOrderByEventDateDesc(LocalDate.now());
    }
    
    public Optional<Event> getEventById(Long id) {
        return eventRepository.findById(id);
    }
    
    public Event updateEvent(Event event) {
        event.setUpdatedAt(LocalDateTime.now());
        return eventRepository.save(event);
    }
    
    public void deleteEvent(Long id) {
        eventRepository.deleteById(id);
    }
    
    public List<Event> searchEvents(String keyword) {
        return eventRepository.findByKeyword(keyword);
    }
    
    public List<Event> getEventsByCategory(Event.EventCategory category) {
        return eventRepository.findByCategory(category);
    }
    
    public List<Event> getEventsByLocation(String location) {
        return eventRepository.findByLocationContainingIgnoreCase(location);
    }
    
    public List<Event> getEventsByDateRange(LocalDate startDate, LocalDate endDate) {
        return eventRepository.findByEventDateBetween(startDate, endDate);
    }
    
    public void decreaseAvailableSeats(Event event) {
        if (event.getAvailableSeats() > 0) {
            event.setAvailableSeats(event.getAvailableSeats() - 1);
            updateEvent(event);
        }
    }
    
    public void increaseAvailableSeats(Event event) {
        if (event.getAvailableSeats() < event.getCapacity()) {
            event.setAvailableSeats(event.getAvailableSeats() + 1);
            updateEvent(event);
        }
    }
    
    
}

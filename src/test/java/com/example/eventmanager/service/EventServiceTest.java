package com.example.eventmanager.service;

import com.example.eventmanager.model.Event;
import com.example.eventmanager.repository.EventRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EventServiceTest {

    @Mock
    private EventRepository eventRepository;

    @InjectMocks
    private EventService eventService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateEvent() {
        Event event = new Event();
        event.setName("Tech Meetup");
        event.setCapacity(100);

        when(eventRepository.save(any(Event.class))).thenReturn(event);

        Event saved = eventService.createEvent(event);

        assertNotNull(saved);
        assertEquals("Tech Meetup", saved.getName());
        assertEquals(100, saved.getCapacity());
        verify(eventRepository, times(1)).save(event);
    }

    @Test
    void testGetUpcomingEvents() {
        Event event = new Event();
        event.setName("Future Event");
        event.setEventDate(LocalDate.now().plusDays(10));

        when(eventRepository.findByEventDateAfterOrderByEventDateAsc(any(LocalDate.class)))
                .thenReturn(Collections.singletonList(event));

        List<Event> upcoming = eventService.getUpcomingEvents();

        assertEquals(1, upcoming.size());
        assertEquals("Future Event", upcoming.get(0).getName());
    }

    @Test
    void testGetEventById() {
        Event event = new Event();
        event.setId(1L);
        event.setName("Sample Event");

        when(eventRepository.findById(1L)).thenReturn(Optional.of(event));

        Optional<Event> found = eventService.getEventById(1L);

        assertTrue(found.isPresent());
        assertEquals("Sample Event", found.get().getName());
    }
}

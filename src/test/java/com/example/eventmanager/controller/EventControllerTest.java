package com.example.eventmanager.controller;

import com.example.eventmanager.model.Event;
import com.example.eventmanager.service.EventRegistrationService;
import com.example.eventmanager.service.EventService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(EventController.class)
class EventControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EventService eventService;

    @MockBean
    private EventRegistrationService registrationService;

    @Test
    void testListEvents() throws Exception {
        Event event = new Event();
        event.setId(1L);
        event.setName("Spring Boot Workshop");
        event.setEventDate(LocalDate.now().plusDays(5));

        Mockito.when(eventService.getUpcomingEvents()).thenReturn(Collections.singletonList(event));

        mockMvc.perform(get("/events"))
                .andExpect(status().isOk())
                .andExpect(view().name("events/list"))
                .andExpect(model().attributeExists("events"));
    }

    @Test
    void testViewEvent() throws Exception {
        Event event = new Event();
        event.setId(1L);
        event.setName("Test Event");

        Mockito.when(eventService.getEventById(anyLong())).thenReturn(Optional.of(event));
        Mockito.when(registrationService.isUserRegistered(Mockito.any(), Mockito.eq(event))).thenReturn(false);
        Mockito.when(registrationService.getRegistrationCount(event)).thenReturn(5L);

        mockMvc.perform(get("/events/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("events/detail"))
                .andExpect(model().attributeExists("event"))
                .andExpect(model().attributeExists("isRegistered"))
                .andExpect(model().attributeExists("registrationCount"));
    }
}

package com.example.eventmanager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.eventmanager.model.Event;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {

    List<Event> findByEventDateAfterOrderByEventDateAsc(LocalDate date);

    List<Event> findByEventDateBeforeOrderByEventDateDesc(LocalDate date);

    List<Event> findByCategory(Event.EventCategory category);

    List<Event> findByLocationContainingIgnoreCase(String location);

    @Query("SELECT e FROM Event e WHERE e.name LIKE %:keyword% OR e.description LIKE %:keyword% OR e.location LIKE %:keyword%")
    List<Event> findByKeyword(@Param("keyword") String keyword);

    @Query("SELECT e FROM Event e WHERE e.eventDate >= :startDate AND e.eventDate <= :endDate")
    List<Event> findByEventDateBetween(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
}

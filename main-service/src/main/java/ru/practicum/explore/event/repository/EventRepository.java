package ru.practicum.explore.event.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.explore.event.model.Event;
import ru.practicum.explore.event.model.Status;

import java.time.LocalDateTime;
import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {

    @Query("select e from Event e " +
            "where ((e.annotation LIKE %?1%)" +
            "OR (e.description LIKE %?1%)) " +
            "AND e.category.id IN ?2 " +
            "AND e.paid=?3 " +
            "AND e.eventDate>=?4 " +
            "AND e.eventDate<=?5 " +
            "order by e.id")
    List<Event> getAllEventsByParameters(String text, List<Long> catIds, Boolean paid,
                                         LocalDateTime rangeStart, LocalDateTime rangeEnd, Pageable pageable);

    List<Event> findAllByInitiator_IdOrderById(Long userId, Pageable pageable);

    @Query("select e from Event e " +
            "where e.initiator.id IN ?1 " +
            "AND e.state IN ?2 " +
            "AND e.category.id IN ?3 " +
            "AND e.eventDate>=?4 " +
            "AND e.eventDate<=?5 " +
            "order by e.id")
    List<Event> getAllEvents(List<Long> users, List<Status> states, List<Long> categories,
                             LocalDateTime rangeStart, LocalDateTime rangeEnd, Pageable pageable);
}

package ewmservice.event.repository;

import ewmservice.event.model.Event;
import ewmservice.event.model.EventState;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {

    @Query("SELECT e FROM Event e WHERE e.initiator IN (:users) AND e.category IN (:categories) " +
            "AND e.state IN (:states) AND e.createdOn > :range_start AND e.createdOn < :range_end")
    List<Event> findAllWithParameters(@Param("users") List<Long> users,
                                      @Param("states") List<EventState> states,
                                      @Param("categories") List<Long> categories,
                                      @Param("range_start") LocalDateTime rangeStart,
                                      @Param("range_end") LocalDateTime rangeEnd,
                                      Pageable pageable);

    @Query("SELECT e FROM Event e WHERE e.annotation IN (:text) OR e.title IN (:text) " +
            "AND e.category IN (:categories) AND e.paid = :paid " +
            "AND e.createdOn > :range_start AND e.createdOn < :range_end")
    List<Event> findAllWithMoreRequirements(@Param("text") String text,
                                            @Param("categories") List<Long> categories,
                                            @Param("paid") Boolean paid,
                                            @Param("range_start") LocalDateTime rangeStart,
                                            @Param("range_end") LocalDateTime rangeEnd,
                                            Pageable pageable);

    @Query("SELECT e FROM Event e WHERE e.id=:event_id AND e.initiator=:user_id")
    Event findByIdAndInitiator(@Param("user_id") Long userId, @Param("event_id") Long eventId);

    @Query("SELECT e FROM Event e WHERE e.initiator=:user_id")
    List<Event> findAllByInitiator(@Param("user_id") Long userId, Pageable pageable);

    @Query("SELECT e FROM Event e WHERE e.initiator=:user_id AND e.eventDate>:time")
    List<Event> findAllByInitiator(@Param("user_id") Long userId, @Param("time") LocalDateTime time);
}

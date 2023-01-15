package ru.practicum.explore.request.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.explore.request.mapper.model.Request;
import ru.practicum.explore.request.model.StatusRequest;

import java.util.List;

@Repository
public interface RequestRepository extends JpaRepository<Request, Long> {

    @Query("select pr from Request  pr where pr.event.id=?1 and pr.event.initiator.id=?2")
    List<Request> findAllByEvent(Long eventId, Long userId);

    Integer countByEvent_IdAndStatus(Long eventId, StatusRequest confirmed);

    List<Request> findAllByRequester_IdOrderById(Long userId);
}

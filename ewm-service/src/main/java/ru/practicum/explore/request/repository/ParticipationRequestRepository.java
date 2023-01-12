package ru.practicum.explore.request.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.explore.request.mapper.model.ParticipationRequest;
import ru.practicum.explore.request.model.StatusRequest;

import java.util.List;

/**
 * Интерфейс репозитория запросов на участия
 */
public interface ParticipationRequestRepository extends JpaRepository<ParticipationRequest, Long> {
    /*
     Метод получения всех запросов на участие в событиях  по id события и id тользователя
    */
    @Query("select pr from ParticipationRequest  pr where pr.event.id=?1 and pr.event.initiator.id=?2")
    List<ParticipationRequest> findAllByEvent(Long eventId, Long userId);

    /*
    Метод получения числа одобренных заявок на участия в событие
    */
    Integer countByEvent_IdAndStatus(Long eventId, StatusRequest confirmed);

    /*
    Метод получения всех запросов на участие текущим пользователем по id
    */
    List<ParticipationRequest> findAllByRequester_IdOrderById(Long userId);
}

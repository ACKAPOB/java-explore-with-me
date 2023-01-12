package ru.practicum.explore.request.mapper;

import ru.practicum.explore.request.dto.ParticipationRequestDto;
import ru.practicum.explore.request.mapper.model.ParticipationRequest;

/**
 * Интерфейс маппера Запросов на участие в событие
 */
public interface RequestMapper {
    /*
    Метод маппера для получения dto запроса из модели запроса на участие в событие
    */
    ParticipationRequestDto toParticipationRequestDto(ParticipationRequest participationRequest);
}

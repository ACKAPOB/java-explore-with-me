package ru.practicum.explore.request.mapper;

import ru.practicum.explore.request.dto.ParticipationRequestDto;
import ru.practicum.explore.request.mapper.model.ParticipationRequest;

public interface RequestMapper {

    ParticipationRequestDto toParticipationRequestDto(ParticipationRequest participationRequest);
}

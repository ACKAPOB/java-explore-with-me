package ru.practicum.explore.user.service;

import ru.practicum.explore.event.dto.EventFullDto;
import ru.practicum.explore.event.dto.EventShortDto;
import ru.practicum.explore.event.dto.NewEventDto;
import ru.practicum.explore.event.dto.UpdateEventRequest;
import ru.practicum.explore.request.dto.ParticipationRequestDto;

import java.util.Collection;

public interface UserService {

    Collection<EventShortDto> findAllEventsByUserId(Long userId, Integer from, Integer size);

    EventFullDto patchEventByUser(Long userId, UpdateEventRequest updateEventRequest);

    EventFullDto postEvent(Long userId, NewEventDto newEventDto);

    EventFullDto getEventFull(Long userId, Long eventId);

    EventFullDto cancelEvent(Long userId, Long eventId);

    Collection<ParticipationRequestDto> getRequestByUser(Long userId, Long eventId);

    ParticipationRequestDto approveConfirmUserByEvent(Long userId, Long eventId, Long reqId);

    ParticipationRequestDto approveRejectUserByEvent(Long userId, Long eventId, Long reqId);

    Collection<ParticipationRequestDto> getRequestsByUser(Long userId);

    ParticipationRequestDto postRequestUser(Long userId, Long eventId);

    ParticipationRequestDto cancelRequestByUser(Long userId, Long requestId);
}

package ru.practicum.explore.event.service;

import ru.practicum.explore.event.dto.*;
import ru.practicum.explore.request.dto.RequestDto;

import java.util.Collection;
import java.util.List;

public interface PrivatEventService {

    Collection<EventShortDto> findAll(Long userId, Integer from, Integer size);

    EventFullDto patch(Long userId, UpdateEventRequest updateEventRequest);

    EventFullDto post(Long userId, NewEventDto newEventDto);

    EventFullDto getEventFull(Long userId, Long eventId);

    EventFullDto cancelEvent(Long userId, Long eventId);

    List<RequestDto> getRequest(Long userId, Long eventId);

    RequestDto confirmUserByEvent(Long userId, Long eventId, Long reqId);

    RequestDto rejectUserByEvent(Long userId, Long eventId, Long reqId);
}

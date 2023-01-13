package ru.practicum.explore.user.service;

import ru.practicum.explore.event.dto.EventFullDto;
import ru.practicum.explore.event.dto.EventShortDto;
import ru.practicum.explore.event.dto.NewEventDto;
import ru.practicum.explore.event.dto.UpdateEventRequest;
import ru.practicum.explore.request.dto.RequestDto;

import java.util.Collection;
import java.util.List;

public interface UserService {

    Collection<EventShortDto> findAllEventsByUserId(Long userId, Integer from, Integer size);

    EventFullDto patchEventByUser(Long userId, UpdateEventRequest updateEventRequest);

    EventFullDto postEvent(Long userId, NewEventDto newEventDto);

    EventFullDto getEventFull(Long userId, Long eventId);

    EventFullDto cancelEvent(Long userId, Long eventId);

    List<RequestDto> getRequestByUser(Long userId, Long eventId);

    RequestDto approveConfirmUserByEvent(Long userId, Long eventId, Long reqId);

    RequestDto approveRejectUserByEvent(Long userId, Long eventId, Long reqId);
}

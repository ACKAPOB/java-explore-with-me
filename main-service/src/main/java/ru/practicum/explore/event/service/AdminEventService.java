package ru.practicum.explore.event.service;

import ru.practicum.explore.event.dto.AdminUpdateEventRequest;
import ru.practicum.explore.event.dto.EventFullDto;
import ru.practicum.explore.event.model.Status;

import java.time.LocalDateTime;
import java.util.List;

public interface AdminEventService {

    List<EventFullDto> getAllEvents(List<Long> users, List<Status> states, List<Long> categories,
                                    LocalDateTime rangeStart, LocalDateTime rangeEnd, Integer from, Integer size);

    EventFullDto putEvent(Long eventId, AdminUpdateEventRequest adminUpdateEventRequest);

    EventFullDto approvePublishEvent(Long eventId);

    EventFullDto approveRejectEvent(Long eventId);
}

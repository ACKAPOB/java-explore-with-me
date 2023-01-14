package ru.practicum.explore.event.service;

import ru.practicum.explore.event.dto.AdminUpdateEventRequest;
import ru.practicum.explore.event.dto.EventFullDto;
import ru.practicum.explore.event.model.Status;

import java.util.List;

public interface AdminEventService {

    List<EventFullDto> getAllEventsAdmin(List<Long> users, List<Status> states, List<Long> categories,
                                         String rangeStart, String rangeEnd, Integer from, Integer size);

    EventFullDto putEvent(Long eventId, AdminUpdateEventRequest adminUpdateEventRequest);

    EventFullDto approvePublishEvent(Long eventId);

    EventFullDto approveRejectEvent(Long eventId);
}

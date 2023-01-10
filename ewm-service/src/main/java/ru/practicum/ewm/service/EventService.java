package ru.practicum.ewm.service;

import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.dto.EventFullDto;
import ru.practicum.ewm.dto.EventShortDto;
import ru.practicum.ewm.dto.NewEventDto;
import ru.practicum.ewm.dto.RequestDto;
import ru.practicum.ewm.model.EventState;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface EventService {
    EventFullDto saveEvent(Long userId, NewEventDto newEventDto);

    EventFullDto publishEvent(Long eventId);

    EventFullDto updateEventByAdmin(Long eventId, NewEventDto newEventDto);

    EventFullDto updateEventByInitiator(Long userId, NewEventDto newEventDto);

    EventFullDto denyEventByAdmin(Long eventId);

    EventFullDto denyEventByUser(Long userId, Long eventId);

    List<EventFullDto> getEvents(List<Long> users, List<EventState> states, List<Long> categories, String rangeStart,
                                         String rangeEnd, Integer from, Integer size);

    List<EventShortDto> getSortedEvents(String text, List<Long> categories, Boolean paid, String rangeStart,
                                         String rangeEnd, Integer from, Integer size, HttpServletRequest request);

    EventFullDto getEventById(Long id, HttpServletRequest request);

    EventFullDto getEventForUser(Long userId, Long eventId);

    List<EventFullDto> getEventsForInitiator(Long userId, Integer from, Integer size);

    @Transactional
    List<RequestDto> getInfoAboutRequestsForEventOwner(Long userId, Long eventId);

    @Transactional
    RequestDto confirmRequest(Long userId, Long eventId, Long reqId);

    @Transactional
    RequestDto rejectRequestByOwner(Long userId, Long eventId, Long reqId);
}

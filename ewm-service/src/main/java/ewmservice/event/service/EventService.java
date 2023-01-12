package ewmservice.event.service;

import ewmservice.event.dto.EventFullDto;
import ewmservice.event.dto.EventShortDto;
import ewmservice.event.dto.NewEventDto;
import ewmservice.event.model.EventState;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface EventService {
    EventFullDto saveEvent(Long userId, NewEventDto newEventDto);

    EventFullDto publishEvent(Long eventId);

    EventFullDto updateEventByAdmin(Long eventId, NewEventDto newEventDto);

    EventFullDto updateEventByInitiator(Long userId, NewEventDto newEventDto);

    EventFullDto denyEventByAdmin(Long eventId);

    EventFullDto denyEventByInitiator(Long userId, Long eventId);

    List<EventFullDto> getEventsForAdmin(List<Long> users, List<EventState> states,
                                         List<Long> categories, String rangeStart,
                                         String rangeEnd, Integer from, Integer size);

    List<EventShortDto> getEventsForUser(String text, List<Long> categories,
                                         Boolean paid, String rangeStart,
                                         String rangeEnd, Integer from,
                                         Integer size, HttpServletRequest request);

    EventFullDto getEventById(Long id, HttpServletRequest request);

    EventFullDto getEventForInitiator(Long userId, Long eventId);

    List<EventFullDto> getEventsForInitiator(Long userId, Integer from, Integer size);
}

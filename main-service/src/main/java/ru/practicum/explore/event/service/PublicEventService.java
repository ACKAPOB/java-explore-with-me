package ru.practicum.explore.event.service;

import ru.practicum.explore.event.dto.EventFullDto;
import ru.practicum.explore.event.dto.EventShortDto;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;

public interface PublicEventService {


    List<EventShortDto> getAllEvent(String text, List<Long> categories, Boolean paid, String rangeStart,
                                    String rangeEnd, Boolean onlyAvailable, String sort, Integer from,
                                    Integer size);

    Optional<EventFullDto> getEvent(Long eventId);

    void saveInStatService(HttpServletRequest request);
}

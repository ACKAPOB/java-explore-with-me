package ru.practicum.explore.event.service.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explore.clients.StatsClient;
import ru.practicum.explore.event.dto.EventFullDto;
import ru.practicum.explore.event.dto.EventShortDto;
import ru.practicum.explore.event.mapper.EventMapper;
import ru.practicum.explore.clients.ViewStats;
import ru.practicum.explore.event.model.Event;
import ru.practicum.explore.event.model.EventSort;
import ru.practicum.explore.event.repository.EventRepository;
import ru.practicum.explore.event.service.PublicEventService;
import ru.practicum.explore.exception.ObjectNotFoundException;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@AllArgsConstructor
@Transactional(readOnly = true)
public class PublicEventServiceImpl implements PublicEventService {
    private final EventRepository eventRepository;
    private final EventMapper eventMapper;
    private final StatsClient statsClient;

    @Override
    public List<EventShortDto> getAllEvent(String text, List<Long> categories, Boolean paid, LocalDateTime rangeStart,
                                           LocalDateTime rangeEnd, Boolean onlyAvailable, EventSort sort, Integer from,
                                           Integer size, HttpServletRequest request) {
        log.info("Получение событий с возможностью фильтрации PublicEventServiceImpl.getAllEvent text = {}", text);
        statsClient.save(request);
        Pageable pageSorted = PageRequest.of(from, size, Sort.by(sort.toString()).descending());

        Pageable pageable = PageRequest.of(from / size, size, Sort.by("id").descending());
        List<EventShortDto> listEvent = eventRepository
                .getAllEventsByParameters(text, categories, paid, rangeStart, rangeEnd, pageSorted)
                .stream()
                .map(eventMapper::toEventShortDto)
                .collect(Collectors.toList());
        return listEvent;
    }

    @Override
    public Optional<EventFullDto> getEvent(Long eventId, HttpServletRequest request) {
        log.info("Получение подробной информации об опубликованном событии по его идентификатору " +
                "PublicEventServiceImpl.getEvent id={}", eventId);
        Event event = eventRepository
                .findById(eventId)
                .orElseThrow(() -> new ObjectNotFoundException(String.format("Event not found id = %s", eventId)));
        List<ViewStats> responseBody = statsClient.getViews(Collections.singletonList(event), true);
        if (!responseBody.isEmpty()) {
            event.setViews(responseBody.get(0).getHits());
        }
        statsClient.save(request);
        return Optional.of(eventRepository.findById(eventId).map(eventMapper::toEventFullDto)
                .orElseThrow(() -> new ObjectNotFoundException(String.format("Event not found id = %s", eventId))));
    }
}
/*        BooleanExpression predicate = getPredicate(text, categories, paid, rangeStart, rangeEnd, onlyAvailable);
        if (eventSort.equals(EventSort.EVENT_DATE)) {
            Pageable pageSortedByDate = PageRequest.of(from, size, Sort.by("eventDate").descending());
            return EventDtoMapper.toEventShortDto(eventRepository.findAll(predicate, pageSortedByDate).getContent());
        } else {
            Pageable pageSortedByViews = PageRequest.of(from, size, Sort.by("views").descending());
            return EventDtoMapper.toEventShortDto(eventRepository.findAll(predicate, pageSortedByViews).getContent());
        }

 */
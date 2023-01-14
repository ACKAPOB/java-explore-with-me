package ru.practicum.explore.event.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.explore.clients.EndpointHit;
import ru.practicum.explore.clients.StatClient;
import ru.practicum.explore.event.dto.EventFullDto;
import ru.practicum.explore.event.dto.EventShortDto;
import ru.practicum.explore.event.mapper.EventMapper;
import ru.practicum.explore.event.repository.EventRepository;
import ru.practicum.explore.event.service.PublicEventService;
import ru.practicum.explore.exception.ObjectNotFoundException;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class PublicEventServiceImpl implements PublicEventService {
    private final EventRepository eventRepository;
    private final EventMapper eventMapper;
    private final StatClient statClient;

    public PublicEventServiceImpl(EventRepository eventRepository, EventMapper eventMapper, StatClient statClient) {
        this.eventRepository = eventRepository;
        this.eventMapper = eventMapper;
        this.statClient = statClient;
    }

    @Override
    public List<EventShortDto> getAllEvent(String text, List<Long> categories, Boolean paid, String rangeStart,
                                           String rangeEnd, Boolean onlyAvailable, String sort, Integer from,
                                           Integer size) {
        log.info("Получение событий с возможностью фильтрации PublicEventServiceImpl.getAllEvent text = {}", text);
        Pageable pageable = PageRequest.of(from / size, size, Sort.by("id").descending());
        LocalDateTime start = LocalDateTime.parse(rangeStart, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        LocalDateTime end = LocalDateTime.parse(rangeEnd, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        List<EventShortDto> listEvent = eventRepository
                .getAllEventsByParameters(text, categories, paid, start, end, pageable)
                .stream()
                .map(eventMapper::toEventShortDto)
                .collect(Collectors.toList());
        if (sort.equals("EVENT_DATE")) {
            listEvent.stream().sorted(Comparator.comparing(EventShortDto::getEventDate));
        }
        if (sort.equals("VIEWS")) {
            listEvent.stream().sorted(Comparator.comparing(EventShortDto::getViews));
        }
        return listEvent;
    }

    @Override
    public Optional<EventFullDto> getEvent(Long eventId) {
        log.info("Получение подробной информации об опубликованном событии по его идентификатору " +
                "PublicEventServiceImpl.getEvent id={}", eventId);
        return Optional.of(eventRepository.findById(eventId).map(eventMapper::toEventFullDto)
                .orElseThrow(() -> new ObjectNotFoundException(String.format("Event not found id = %s", eventId))));
    }

    @Override
    public void saveInStatService(HttpServletRequest request) {
        log.info("Отпарвление данных в стат сервер EventServiceImpl.getEvent id={}", request);
        EndpointHit endpointHit = EndpointHit.builder()
                .app("main-service")
                .uri(request.getRequestURI())
                .ip(request.getRemoteAddr())
                .timestamp(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .build();
        statClient.save(endpointHit);
    }

}

package ru.practicum.explore.event.service.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explore.category.model.Category;
import ru.practicum.explore.category.repository.CategoryRepository;
import ru.practicum.explore.event.dto.AdminUpdateEventRequest;
import ru.practicum.explore.event.dto.EventFullDto;
import ru.practicum.explore.event.mapper.EventMapper;
import ru.practicum.explore.event.model.Event;
import ru.practicum.explore.event.model.Status;
import ru.practicum.explore.event.repository.EventRepository;
import ru.practicum.explore.event.service.AdminEventService;
import ru.practicum.explore.exception.ErrorRequestException;
import ru.practicum.explore.exception.ObjectNotFoundException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@Slf4j
@AllArgsConstructor
public class AdminEventServiceImpl implements AdminEventService {
    private final EventRepository eventRepository;
    private final EventMapper eventMapper;
    private final CategoryRepository categoryRepository;

    @Override
    public List<EventFullDto> getAll(List<Long> users, List<Status> states, List<Long> categories,
                                     LocalDateTime rangeStart, LocalDateTime rangeEnd, Integer from, Integer size) {
        log.info("Поиск событийAdmin AdminEventServiceImpl.getAll users = {}", users);
        Pageable pageable = PageRequest.of(from / size, size, Sort.by("id").descending());
        return eventRepository.getAllEvents(users, states, categories, rangeStart, rangeEnd, pageable)
                .stream()
                .map(eventMapper::toEventFullDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public EventFullDto put(Long eventId, AdminUpdateEventRequest adminUpdateEventRequest) {
        log.info("Редактирование события eventId = {} AdminEventServiceImpl.put", eventId);
        LocalDateTime eventDate = LocalDateTime.parse(adminUpdateEventRequest.getEventDate(),
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        if (!eventDate.isAfter(LocalDateTime.now().minusHours(2)))
            throw new ErrorRequestException("Time is not correct");
        Event event = eventRepository
                .findById(eventId)
                .orElseThrow(() -> new ObjectNotFoundException(String.format("Событие не найдено" +
                        " put id = %s", eventId)));
        Category category = categoryRepository.findById(adminUpdateEventRequest.getCategory())
                .orElseThrow(() -> new ObjectNotFoundException(String.format("Категория не " +
                        "найдена put id = %s", eventId)));
        event.setCategory(category);
        event.setEventDate(eventDate);
        Optional.ofNullable(adminUpdateEventRequest.getAnnotation()).ifPresent(event::setAnnotation);
        Optional.ofNullable(adminUpdateEventRequest.getDescription()).ifPresent(event::setDescription);
        Optional.ofNullable(adminUpdateEventRequest.getLocation()).ifPresent(event::setLocation);
        Optional.ofNullable(adminUpdateEventRequest.getPaid()).ifPresent(event::setPaid);
        Optional.ofNullable(adminUpdateEventRequest.getParticipantLimit()).ifPresent(event::setParticipantLimit);
        Optional.ofNullable(adminUpdateEventRequest.getRequestModeration()).ifPresent(event::setRequestModeration);
        Optional.ofNullable(adminUpdateEventRequest.getTitle()).ifPresent(event::setTitle);
        eventRepository.save(event);
        return eventMapper.toEventFullDto(event);
    }

    @Override
    @Transactional
    public EventFullDto publishEvent(Long eventId) {
        log.info("Публикация события eventId = {} AdminEventServiceImpl.publishEvent", eventId);
        Event event = eventRepository
                .findById(eventId)
                .orElseThrow(() -> new ObjectNotFoundException(String.format("Событие не найдено id = %s", eventId)));
        event.setState(Status.PUBLISHED);
        eventRepository.save(event);
        log.info("Проверка  Status.PUBLISHED = {} AdminEventServiceImpl.publishEvent", event.getState());
        return eventMapper.toEventFullDto(event);
    }

    @Override
    @Transactional
    public EventFullDto rejectEvent(Long eventId) {
        log.info("Отклонение события AdminEventServiceImpl.rejectEvent eventId = {}", eventId);
        Event event = eventRepository
                .findById(eventId)
                .orElseThrow(() -> new ObjectNotFoundException(String.format("Событие не найдено rejectEvent " +
                        "id = %s", eventId)));
        event.setState(Status.CANCELED);
        eventRepository.save(event);
        return eventMapper.toEventFullDto(event);
    }

}

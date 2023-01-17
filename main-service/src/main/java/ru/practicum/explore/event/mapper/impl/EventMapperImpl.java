package ru.practicum.explore.event.mapper.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.practicum.explore.category.mapper.CategoryMapper;
import ru.practicum.explore.event.dto.*;
import ru.practicum.explore.event.mapper.EventMapper;
import ru.practicum.explore.feature.comment.mapper.CommentMapper;
import ru.practicum.explore.feature.comment.repository.CommentRepository;
import ru.practicum.explore.user.mapper.UserMapper;
import ru.practicum.explore.category.model.Category;
import ru.practicum.explore.event.model.Event;
import ru.practicum.explore.location.model.Location;
import ru.practicum.explore.user.model.User;
import ru.practicum.explore.request.repository.RequestRepository;
import ru.practicum.explore.request.model.StatusRequest;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@Slf4j
public class EventMapperImpl implements EventMapper {
    private final CategoryMapper categoryMapper;
    private final UserMapper userMapper;
    private final RequestRepository requestRepository;
    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;

    public EventMapperImpl(CategoryMapper categoryMapper, UserMapper userMapper,
                           RequestRepository requestRepository, CommentRepository commentRepository,
                           CommentMapper commentMapper) {
        this.categoryMapper = categoryMapper;
        this.userMapper = userMapper;
        this.requestRepository = requestRepository;
        this.commentRepository = commentRepository;
        this.commentMapper = commentMapper;
    }

    public Integer getConfirmedRequests(Long id) {
        return requestRepository.countByEvent_IdAndStatus(id, StatusRequest.CONFIRMED);
    }

    @Override
    public EventFullDto toEventFullDto(Event event) {
        log.info("Запуск toEventFullDto event = {}", event);
        return EventFullDto.builder()
                .id(event.getId())
                .annotation(event.getAnnotation())
                .category(categoryMapper.toCategoryDto(event.getCategory()))
                .description(event.getDescription())
                .eventDate(event.getEventDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .createdOn(event.getCreatedOn().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .initiator(userMapper.toUserShortDto(event.getInitiator()))
                .location(event.getLocation())
                .paid(event.getPaid())
                .participantLimit(event.getParticipantLimit())
                .confirmedRequests(getConfirmedRequests(event.getId()))
                .publishedOn(event.getPublishedOn() != null ? event
                        .getPublishedOn()
                        .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) : null)
                .requestModeration(event.getRequestModeration())
                .state(event.getState())
                .title(event.getTitle())
                .views(event.getViews())
                .comments(commentRepository.findAllByEventOrderByCreated(event)
                        .stream()
                        .map(commentMapper::toCommentDto)
                        .collect(Collectors.toList()))
                .build();
    }

    @Override
    public EventShortDto toEventShortDto(Event event) {
        return EventShortDto.builder()
                .id(event.getId())
                .annotation(event.getAnnotation())
                .category(categoryMapper.toCategoryDto(event.getCategory()))
                .eventDate(event.getEventDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .initiator(userMapper.toUserShortDto(event.getInitiator()))
                .confirmedRequests(getConfirmedRequests(event.getId()))
                .paid(event.getPaid())
                .title(event.getTitle())
                .views(event.getViews())
                .comments(commentRepository.findAllByEventOrderByCreated(event)
                        .stream()
                        .map(commentMapper::toCommentDto)
                        .collect(Collectors.toList()))
                .build();
    }

    @Override
    public Event toEvent(NewEventDto newEventDto, User user,
                         Location location, Category category, LocalDateTime eventDate) {
        return Event.builder()
                .annotation(newEventDto.getAnnotation())
                .category(category)
                .description(newEventDto.getDescription())
                .createdOn(LocalDateTime.now())
                .eventDate(eventDate)
                .location(location)
                .initiator(user)
                .paid(newEventDto.getPaid())
                .participantLimit(newEventDto.getParticipantLimit())
                .requestModeration(newEventDto.getRequestModeration())
                .title(newEventDto.getTitle())
                .build();
    }

    @Override
    public void updateEventFromNewEventDto(UpdateEventRequest newEventDto, Event event) {
        Optional.ofNullable(newEventDto.getAnnotation()).ifPresent(event::setAnnotation);
        Optional.ofNullable(newEventDto.getDescription()).ifPresent(event::setDescription);
        if (newEventDto.getEventDate() != null)
            event.setEventDate(LocalDateTime.parse(newEventDto.getEventDate(),
                    DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        Optional.ofNullable(newEventDto.getPaid()).ifPresent(event::setPaid);
        Optional.ofNullable(newEventDto.getParticipantLimit()).ifPresent(event::setParticipantLimit);
        Optional.ofNullable(newEventDto.getTitle()).ifPresent(event::setTitle);
    }
}

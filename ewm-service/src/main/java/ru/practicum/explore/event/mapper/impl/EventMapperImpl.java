package ru.practicum.explore.event.mapper.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import ru.practicum.explore.clients.stat.StatClient;
import ru.practicum.explore.category.mapper.CategoryMapper;
import ru.practicum.explore.comment.comment.CommentMapper;
import ru.practicum.explore.event.dto.*;
import ru.practicum.explore.event.mapper.EventMapper;
import ru.practicum.explore.user.mapper.UserMapper;
import ru.practicum.explore.category.model.Category;
import ru.practicum.explore.event.model.Event;
import ru.practicum.explore.location.model.Location;
import ru.practicum.explore.user.model.User;
import ru.practicum.explore.comment.comment.rep.CommentRepository;
import ru.practicum.explore.request.repository.ParticipationRequestRepository;
import ru.practicum.explore.request.model.StatusRequest;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Optional;

@Component
public class EventMapperImpl implements EventMapper {
    private final CategoryMapper categoryMapper;
    private final UserMapper userMapper;
    private final StatClient statClient;
    private final ParticipationRequestRepository participationRequestRepository;
    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;

    @Autowired
    public EventMapperImpl(CategoryMapper categoryMapper, UserMapper userMapper, StatClient statClient,
                           ParticipationRequestRepository participationRequestRepository,
                           CommentRepository commentRepository, CommentMapper commentMapper) {
        this.categoryMapper = categoryMapper;
        this.userMapper = userMapper;
        this.statClient = statClient;
        this.participationRequestRepository = participationRequestRepository;
        this.commentRepository = commentRepository;
        this.commentMapper = commentMapper;
    }

    public Integer getViews(Long id) {
        String start = LocalDateTime.of(2000, 1, 1, 0, 0, 0)
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        String end = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        ResponseEntity<Object> response = statClient.getStats(start, end, List.of("/events/" + id), false);
        List<LinkedHashMap> collection = (List<LinkedHashMap>) response.getBody();
        if (!collection.isEmpty()) {
            Integer views = (Integer) collection.get(0).get("hits");
            return views;
        }
        return 0;
    }

    public Integer getConfirmedRequests(Long id) {
        Integer limitParticipant = participationRequestRepository.countByEvent_IdAndStatus(id, StatusRequest.CONFIRMED);
        return limitParticipant;
    }

    @Override
    public EventFullDto toEventFullDto(Event event) {
        if (event == null) {
            return null;
        }
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
                .views(getViews(event.getId()))
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
                .views(getViews(event.getId()))
                .build();
    }

    @Override
    public Event toEvent(NewEventDto newEventDto, User user,
                         Location location, Category category, LocalDateTime eventDate) {
        if (newEventDto == null) {
            return null;
        }
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
        if (newEventDto == null) {
            return;
        }
        Optional.ofNullable(newEventDto.getAnnotation()).ifPresent(event::setAnnotation);
        Optional.ofNullable(newEventDto.getDescription()).ifPresent(event::setDescription);

        if (newEventDto.getEventDate() != null) {
            event.setEventDate(LocalDateTime.parse(newEventDto.getEventDate(),
                    DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        }
        Optional.ofNullable(newEventDto.getPaid()).ifPresent(event::setPaid);
        Optional.ofNullable(newEventDto.getParticipantLimit()).ifPresent(event::setParticipantLimit);
        Optional.ofNullable(newEventDto.getTitle()).ifPresent(event::setTitle);

    }

}

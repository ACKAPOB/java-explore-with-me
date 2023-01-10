package ru.practicum.ewm.event.mapper;

import ru.practicum.ewm.event.dto.EventFullDto;
import ru.practicum.ewm.event.dto.EventShortDto;
import ru.practicum.ewm.event.dto.NewEventDto;
import ru.practicum.ewm.category.model.Category;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.user.model.User;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class EventMapper {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static Event toEvent(NewEventDto newEventDto) {
        return Event.builder()
                .id(newEventDto.getId())
                .title(newEventDto.getTitle())
                .annotation(newEventDto.getAnnotation())
                .eventDate(LocalDateTime.parse(newEventDto.getEventDate(), formatter))
                .initiator(newEventDto.getInitiator())
                .paid(newEventDto.getPaid())
                .participantLimit(newEventDto.getParticipantLimit())
                .description(newEventDto.getDescription())
                .category(newEventDto.getCategory())
                .lon(newEventDto.getLocation().getLon())
                .lat(newEventDto.getLocation().getLat())
                .state(newEventDto.getState())
                .createdOn(newEventDto.getCreatedOn())
                .requestModeration(newEventDto.getRequestModeration())
                .build();
    }

    public static EventShortDto toEventShortDto(Event event, Category category, User user) {
        return EventShortDto.builder()
                .id(event.getId())
                .annotation(event.getAnnotation())
                .category(new EventShortDto.CategoryDto(category.getId(), category.getName()))
                .confirmedRequests(event.getConfirmedRequests())
                .eventDate(event.getEventDate().format(formatter))
                .initiator(new EventShortDto.UserShorDto(user.getId(), user.getName()))
                .paid(event.getPaid())
                .title(event.getTitle())
                .build();
    }

    public static Event toEventFromFullDto(EventFullDto eventFullDto) {
        return Event.builder()
                .id(eventFullDto.getId())
                .annotation(eventFullDto.getAnnotation())
                .title(eventFullDto.getTitle())
                .category(eventFullDto.getCategory().getId())
                .paid(eventFullDto.getPaid())
                .eventDate(LocalDateTime.parse(eventFullDto.getEventDate(), formatter))
                .initiator(eventFullDto.getInitiator().getId())
                .description(eventFullDto.getDescription())
                .participantLimit(eventFullDto.getParticipantLimit())
                .confirmedRequests(eventFullDto.getConfirmedRequests())
                .lat(eventFullDto.getLocation().getLat())
                .lon(eventFullDto.getLocation().getLon())
                .state(eventFullDto.getState())
                .createdOn(eventFullDto.getCreatedOn())
                .publishedOn(eventFullDto.getPublishedOn())
                .requestModeration(eventFullDto.getRequestModeration())
                .build();
    }

    public static EventFullDto toEventFullDto(Event event, User user, Category category) {
        return EventFullDto.builder()
                .id(event.getId())
                .title(event.getTitle())
                .annotation(event.getAnnotation())
                .category(new EventFullDto.Category(category.getId(), category.getName()))
                .description(event.getDescription())
                .eventDate(event.getEventDate().format(formatter))
                .initiator(new EventFullDto.UserShortDto(user.getId(), user.getName()))
                .location(new EventFullDto.Location(event.getLat(), event.getLon()))
                .paid(event.getPaid())
                .participantLimit(event.getParticipantLimit())
                .confirmedRequests(event.getConfirmedRequests())
                .state(event.getState())
                .createdOn(event.getCreatedOn())
                .publishedOn(event.getPublishedOn())
                .requestModeration(event.getRequestModeration())
                .build();
    }
}

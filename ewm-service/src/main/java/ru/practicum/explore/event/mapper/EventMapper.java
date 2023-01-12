package ru.practicum.explore.event.mapper;

import ru.practicum.explore.category.model.Category;
import ru.practicum.explore.event.dto.*;
import ru.practicum.explore.event.model.Event;
import ru.practicum.explore.location.model.Location;
import ru.practicum.explore.user.model.User;

import java.time.LocalDateTime;

/**
 * Интерфейс маппера событий
 */
public interface EventMapper {
    /*
    Метод маппера из модели события в dto полной изформации о событие
     */
    EventFullDto toEventFullDto(Event event);

    /*
    Метод маппера из модели события в dto  краткой информации о событие
     */
    EventShortDto toEventShortDto(Event event);

    /*
    Метод маппера по добовлению новой модели события
     */
    Event toEvent(NewEventDto newEventDto, User user, Location location, Category category, LocalDateTime eventDate);

    /*
    Метод маппера обновления модели события пользователем
     */
    void updateEventFromNewEventDto(UpdateEventRequest updateEventRequest, Event event);

}

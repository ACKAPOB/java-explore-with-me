package ru.practicum.ewm.controller.admin;

import lombok.RequiredArgsConstructor;
import ru.practicum.ewm.event.dto.EventFullDto;
import ru.practicum.ewm.event.dto.NewEventDto;
import ru.practicum.ewm.event.model.EventState;
import ru.practicum.ewm.event.service.EventService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequestMapping("/admin/events")
@Slf4j
@RequiredArgsConstructor
public class EventsController {

    private final EventService eventService;

    @GetMapping
    public List<EventFullDto> getEvents(@RequestParam(name = "users", required = false) List<Long> users,
                                        @RequestParam(name = "states", required = false) List<EventState> states,
                                        @RequestParam(name = "categories", required = false) List<Long> categories,
                                        @RequestParam(name = "rangeStart", defaultValue = "1950-01-01 13:30:38") String rangeStart,
                                        @RequestParam(name = "rangeEnd", defaultValue = "#{T(java.time.LocalDateTime).now()" +
                                                ".format(T(java.time.format.DateTimeFormatter)" +
                                                ".ofPattern('yyyy-MM-dd HH:mm:ss'))}") String rangeEnd,
                                        @RequestParam(name = "from", defaultValue = "0") @PositiveOrZero Integer from,
                                        @RequestParam(name = "size", defaultValue = "10") @Positive Integer size) {
        log.info("Поиск событий EventController.getEvents, users={}, states={}, categories={}, rangeStart={}, " +
                "rangeEnd={}, from={}, size={}", users, states, categories, rangeStart, rangeEnd, from, size);
        return eventService.getEvents(users, states, categories, rangeStart, rangeEnd, from, size);
    }

    @PutMapping("/{eventId}")
    public EventFullDto updateEventByAdmin(@PathVariable Long eventId, @RequestBody NewEventDto newEventDto) {
        log.info("Запрос на Редактирование события администратором EventController.updateEvent id {}, " +
                "updateEvent {}", eventId, newEventDto);
        return eventService.updateEventByAdmin(eventId, newEventDto);
    }

    @PatchMapping("/{eventId}/publish")
    public EventFullDto publishEvent(@PathVariable Long eventId) {
        log.info("Запрос на публикацию события  EventController.publishEvent eventId = {}", eventId);
        return eventService.publishEvent(eventId);
    }

    @PatchMapping("/{eventId}/reject")
    public EventFullDto denyEventByAdmin(@PathVariable Long eventId) {
        //Информация для редактирования события администратором. Все поля необязательные. Значение полей не валидируется.
        log.info("Запрос на публикацию события  EventController.denyEventByAdmin eventId = {}", eventId);
        return eventService.denyEventByAdmin(eventId);
    }

}

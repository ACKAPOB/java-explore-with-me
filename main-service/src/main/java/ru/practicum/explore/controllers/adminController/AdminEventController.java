package ru.practicum.explore.controllers.adminController;

import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore.event.dto.AdminUpdateEventRequest;
import ru.practicum.explore.event.dto.EventFullDto;
import ru.practicum.explore.event.model.Status;
import ru.practicum.explore.event.service.impl.AdminEventServiceImpl;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/admin/events")
@Slf4j
public class AdminEventController {

    private final AdminEventServiceImpl eventService;

    public AdminEventController(AdminEventServiceImpl eventService) {

        this.eventService = eventService;
    }

    @GetMapping
    public List<EventFullDto> getAllEvents(
            @RequestParam(required = false) List<Long> users,
            @RequestParam(required = false) List<Status> states,
            @RequestParam(required = false) List<Long> categories,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeStart,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeEnd,
            //@RequestParam(required = false) @DateTimeFormat() LocalDateTime rangeStart,
            //@RequestParam(required = false) @DateTimeFormat() LocalDateTime rangeEnd,
            @RequestParam(defaultValue = "0") Integer from,
            @RequestParam(defaultValue = "10") Integer size) {
        log.info("Поиск событий AdminEventController.getAllEvents users = {}", users);
        return eventService.getAll(users, states, categories, rangeStart, rangeEnd, from, size);
    }

    @PutMapping("/{eventId}")
    public EventFullDto putEvent(@PathVariable Long eventId,
                                 @RequestBody AdminUpdateEventRequest adminUpdateEventRequest) {
        log.info("Редактирование события AdminEventController.putEvent eventid = {}", eventId);
        return eventService.put(eventId, adminUpdateEventRequest);
    }

    @PatchMapping("/{eventId}/publish")
    public EventFullDto approvePublishEvent(@PathVariable Long eventId) {
        log.info(" Публикация события eventId = {} AdminEventController.approvePublishEvent", eventId);
        return eventService.publishEvent(eventId);
    }

    @PatchMapping("/{eventId}/reject")
    public EventFullDto approveRejectEvent(@PathVariable Long eventId) {
        log.info("Отклонение события AdminEventController.approvePublishEvent eventId = {}", eventId);
        return eventService.rejectEvent(eventId);
    }

}



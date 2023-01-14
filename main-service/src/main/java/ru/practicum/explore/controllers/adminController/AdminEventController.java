package ru.practicum.explore.controllers.adminController;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore.event.dto.AdminUpdateEventRequest;
import ru.practicum.explore.event.dto.EventFullDto;
import ru.practicum.explore.event.model.Status;
import ru.practicum.explore.event.service.impl.AdminEventServiceImpl;

import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/admin/events")
@Slf4j
public class AdminEventController {

    private final AdminEventServiceImpl eventService;

    public AdminEventController(AdminEventServiceImpl eventService) {
        this.eventService = eventService;
    }

    @GetMapping("/events")
    public Collection<EventFullDto> getAllEventsAdmin(@RequestParam List<Long> users,
                                                 @RequestParam List<Status> states,
                                                 @RequestParam List<Long> categories,
                                                 @RequestParam String rangeStart,
                                                 @RequestParam String rangeEnd,
                                                 @RequestParam(defaultValue = "0") Integer from,
                                                 @RequestParam(defaultValue = "10") Integer size) {
        log.info("Поиск событий AdminEventController.getAllEvents users = {}", users);
        return eventService.getAllEventsAdmin(users, states, categories, rangeStart, rangeEnd, from, size);
    }

    @PutMapping("/{eventId}")
    public EventFullDto putEvent(@PathVariable Long eventId,
                                 @RequestBody AdminUpdateEventRequest adminUpdateEventRequest) {
        log.info("Редактирование события eventId = {} AdminEventController.putEvent", eventId);
        return eventService.putEvent(eventId, adminUpdateEventRequest);
    }

    @PatchMapping("/{eventId}/publish")
    public EventFullDto approvePublishEvent(@PathVariable Long eventId) {
        log.info(" Публикация события eventId = {} AdminEventController.approvePublishEvent", eventId);
        return eventService.approvePublishEvent(eventId);
    }

    @PatchMapping("/{eventId}/reject")
    public EventFullDto approveRejectEvent(@PathVariable Long eventId) {
        log.info("Отклонение события AdminEventController.approvePublishEvent eventId = {}", eventId);
        return eventService.approveRejectEvent(eventId);
    }

}



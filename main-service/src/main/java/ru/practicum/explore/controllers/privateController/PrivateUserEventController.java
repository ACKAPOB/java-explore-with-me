package ru.practicum.explore.controllers.privateController;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore.event.dto.EventFullDto;
import ru.practicum.explore.event.dto.EventShortDto;
import ru.practicum.explore.event.dto.NewEventDto;
import ru.practicum.explore.event.dto.UpdateEventRequest;
import ru.practicum.explore.event.service.PrivatEventService;
import ru.practicum.explore.event.service.impl.PrivateEventServiceImpl;
import ru.practicum.explore.request.dto.RequestDto;

import javax.validation.Valid;
import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/users/{userId}/events")
@Slf4j
public class PrivateUserEventController {
    private final PrivatEventService eventService;

    public PrivateUserEventController(PrivateEventServiceImpl eventService) {
        this.eventService = eventService;
    }

    @GetMapping
    public Collection<EventShortDto> findAllEventsByUserId(@PathVariable Long userId,
                                                           @RequestParam(defaultValue = "0") Integer from,
                                                           @RequestParam(defaultValue = "10") Integer size) {
        log.info("Получение событий, добавленных текущим пользователем " +
                "PrivateUserEventController.findAllEventsByUserId userId = {}", userId);
        return eventService.findAll(userId, from, size);
    }

    @PatchMapping
    public EventFullDto patchEventByUser(@PathVariable Long userId,
                                         @RequestBody @Valid  UpdateEventRequest updateEventRequest) {
        log.info("Изменение события добавленного текущим пользователем userId = {} " +
                "PrivateUserEventController.patchEventByUser", userId);
        return eventService.patch(userId, updateEventRequest);
    }

    @PostMapping
    public EventFullDto postEvent(@PathVariable Long userId, @Valid @RequestBody NewEventDto newEventDto) {
        log.info("Добавление нового события userId = {} PrivateUserEventController.postEvent", userId);
        return eventService.post(userId, newEventDto);
    }

    @GetMapping("/{eventId}")
    public EventFullDto getEventFull(@PathVariable Long userId, @PathVariable Long eventId) {
        log.info("Получение полной информации о событии добавленном текущим пользователем"  +
                " eventId = {} userId= {} PrivateUserEventController.getEventFull", eventId, userId);
        return eventService.getEventFull(userId, eventId);
    }

    @PatchMapping("/{eventId}")
    public EventFullDto cancelEvent(@PathVariable Long userId, @PathVariable Long eventId) {
        log.info("Отмена события добавленного текущим пользователем eventId={} by userId={} " +
                "PrivateUserEventController.cancelEvent", eventId, userId);
        return eventService.cancelEvent(userId, eventId);
    }

    @GetMapping("/{eventId}/requests")
    public List<RequestDto> getRequestByUser(@PathVariable Long userId, @PathVariable Long eventId) {
        log.info("Получение информации о запросах на участие в событии текущего пользователя " +
                "PrivateUserEventController.getRequestByUser userId = {}, eventId={}", userId, eventId);
        return eventService.getRequest(userId, eventId);
    }

    @PatchMapping("/{eventId}/requests/{reqId}/confirm")
    public RequestDto approveConfirmUserByEvent(@PathVariable Long userId, @PathVariable Long eventId,
                                                @PathVariable Long reqId) {
        log.info("Подтверждение чужой заявки на участие в событии текущего пользователя reqId = {}, userId = {}, " +
                "eventId = {} PrivateUserEventController.approveConfirmUserByEvent", reqId, userId, eventId);
        return eventService.confirmUserByEvent(userId, eventId, reqId);
    }

    @PatchMapping("/{eventId}/requests/{reqId}/reject")
    public RequestDto approveRejectUserByEvent(@PathVariable Long userId, @PathVariable Long eventId,
                                               @PathVariable Long reqId) {
        log.info("Отклонение чужой заявки на участие в событии текущего пользователя. reqId ={}, userId={}, " +
                "eventId={} PrivateUserEventController.approveRejectUserByEvent", reqId, userId, eventId);
        return eventService.rejectUserByEvent(userId, eventId, reqId);
    }
}


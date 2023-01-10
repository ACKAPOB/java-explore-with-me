package ru.practicum.ewm.controller.user;

import lombok.RequiredArgsConstructor;
import ru.practicum.ewm.event.dto.EventFullDto;
import ru.practicum.ewm.event.dto.NewEventDto;
import ru.practicum.ewm.request.dto.RequestDto;
import ru.practicum.ewm.event.service.EventService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequestMapping(path = "/users/{userId}/events")
@Slf4j
@RequiredArgsConstructor
public class PrivateEventController {
    private final EventService eventService;

    @GetMapping
    public List<EventFullDto> getEventsForInitiator(@PathVariable(name = "userId") Long userId,
                                                    @RequestParam(name = "from",
                                                            defaultValue = "0") @PositiveOrZero Integer from,
                                                    @RequestParam(name = "sizer",
                                                            defaultValue = "10") @Positive Integer size) {
        log.info("Получение событий, добавленных текущим пользователем PrivateEventController " +
                "getEventsForInitiator userId={}", userId);
        return eventService.getEventsForInitiator(userId, from, size);
    }

    @PatchMapping
    public EventFullDto updateEventByInitiator(@PathVariable(name = "userId") Long userId,
                                               @RequestBody NewEventDto newEventDto) {
        log.info("Изменение события добавленного текущим пользователем PrivateEventController.updateEventByInitiator " +
                "userId={}", userId);
        return eventService.updateEventByInitiator(userId, newEventDto);
    }

    @PostMapping
    public EventFullDto saveEvent(@PathVariable Long userId, @RequestBody NewEventDto newEventDto) {
        return eventService.saveEvent(userId, newEventDto);
    }

    @GetMapping("/{eventId}")
    public EventFullDto getEventForUser(@PathVariable(name = "userId") Long userId,
                                             @PathVariable(name = "eventId") Long eventId) {
        log.info("Получение полной информации о событии добавленном текущим пользователем " +
                "PrivateEventController.getEventForUser userId={}, eventId = {}", eventId, userId);
        return eventService.getEventForUser(userId, eventId);
    }

    @PatchMapping("/{eventId}")
    public EventFullDto denyEventByUser(@PathVariable(name = "userId") Long userId,
                                        @PathVariable(name = "eventId") Long eventId) {
        log.info("Отмена события добавленного текущим пользователем PrivateEventController.denyEventByInitiator  " +
                "event={}, userId={}", eventId, userId);
        return eventService.denyEventByUser(userId, eventId);
    }

    @GetMapping("/{eventId}/requests")
    public List<RequestDto> getInfoAboutRequestsForEventOwner(@PathVariable(name = "userId") Long userId,
                                                              @PathVariable(name = "eventId") Long eventId) {
        log.info("Получение информации о запросах на участие в событии текущего пользователя userId = {}, " +
                "eventId = {} PrivateEventController.getInfoAboutRequestsForEventOwner", userId, eventId);
        return eventService.getInfoAboutRequestsForEventOwner(userId, eventId);
    }

    //Подтверждение чужой заявки на участие в событии текущего пользователя
    @PatchMapping("/{eventId}/requests/{reqId}/confirm")
    public RequestDto confirmRequest(@PathVariable(name = "userId") Long userId,
                                     @PathVariable(name = "eventId") Long eventId,
                                     @PathVariable(name = "reqId") Long reqId) {
        log.info("Подтверждение чужой заявки на участие в событии текущего пользователя " +
                "userId = {}, eventId = {}, reqId = {} PrivateEventController.confirmRequest", userId, eventId, reqId);
        return eventService.confirmRequest(userId, eventId, reqId);
    }

    //Отклонение чужой заявки на участие в событии текущего пользователя
    @PatchMapping("/{eventId}/requests/{reqId}/reject")
    public RequestDto rejectRequest(@PathVariable(name = "userId") Long userId,
                                    @PathVariable(name = "eventId") Long eventId,
                                    @PathVariable(name = "reqId") Long reqId) {
        log.info("Отклонение чужой заявки на участие в событии текущего пользователя " +
                "userId = {}, eventId = {}, reqId = {}User PrivateEventController.rejectRequest", userId, eventId, reqId);
        return eventService.rejectRequestByOwner(userId, eventId, reqId);
    }

































}

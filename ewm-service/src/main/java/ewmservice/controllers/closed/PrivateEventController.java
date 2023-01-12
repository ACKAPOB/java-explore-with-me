package ewmservice.controllers.closed;

import ewmservice.event.dto.EventFullDto;
import ewmservice.event.dto.NewEventDto;
import ewmservice.event.service.EventServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

import static ewmservice.utilities.Validator.validateEvent;

@RestController
@RequestMapping(path = "/users")
@Slf4j
public class PrivateEventController {
    private final EventServiceImpl eventService;

    public PrivateEventController(EventServiceImpl eventService) {
        this.eventService = eventService;
    }

    @PostMapping("/{userId}/events")
    public EventFullDto saveEvent(@PathVariable Long userId, @RequestBody NewEventDto newEventDto) {
        log.info("Creating userId={}, event={}", userId, newEventDto);
        validateEvent(newEventDto);
        return eventService.saveEvent(userId, newEventDto);
    }

    @PatchMapping("/{userId}/events/{eventId}")
    public EventFullDto denyEventByInitiator(@PathVariable(name = "userId") Long userId,
                                             @PathVariable(name = "eventId") Long eventId) {
        log.info("Deny event={} by initiator={}", eventId, userId);
        return eventService.denyEventByInitiator(userId, eventId);
    }

    @PatchMapping("/{userId}/events")
    public EventFullDto updateEventByInitiator(@PathVariable(name = "userId") Long userId,
                                               @RequestBody NewEventDto newEventDto) {
        log.info("Update event by initiator={}", userId);
        return eventService.updateEventByInitiator(userId, newEventDto);
    }

    @GetMapping("/{userId}/events")
    public List<EventFullDto> getEventsForInitiator(@PathVariable(name = "userId") Long userId,
                                                    @RequestParam(name = "from",
                                                            defaultValue = "0") @PositiveOrZero Integer from,
                                                    @RequestParam(name = "sizer",
                                                            defaultValue = "10") @Positive Integer size) {
        log.info("Get events for initiator={}", userId);
        return eventService.getEventsForInitiator(userId, from, size);
    }


    @GetMapping("/{userId}/events/{eventId}")
    public EventFullDto getEventForInitiator(@PathVariable(name = "userId") Long userId,
                                             @PathVariable(name = "eventId") Long eventId) {
        log.info("Get event={} for initiator={}", eventId, userId);
        return eventService.getEventForInitiator(userId, eventId);
    }
}

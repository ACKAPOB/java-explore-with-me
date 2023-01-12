package ewmservice.controllers.admin;

import ewmservice.event.dto.EventFullDto;
import ewmservice.event.dto.NewEventDto;
import ewmservice.event.model.EventState;
import ewmservice.event.service.EventServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequestMapping(path = "/admin")
@Slf4j
public class EventsController {
    private final EventServiceImpl eventService;

    public EventsController(EventServiceImpl eventService) {
        this.eventService = eventService;
    }

    @PatchMapping("/events/{eventId}/publish")
    public EventFullDto publishEvent(@PathVariable Long eventId) {
        log.info("Publish eventId={}", eventId);
        return eventService.publishEvent(eventId);
    }

    @PatchMapping("/events/{eventId}/reject")
    public EventFullDto denyEventByAdmin(@PathVariable Long eventId) {
        return eventService.denyEventByAdmin(eventId);
    }

    @PutMapping("/events/{eventId}")
    public EventFullDto updateEventByAdmin(@PathVariable Long eventId, @RequestBody NewEventDto newEventDto) {
        log.info("Update event with id={}", eventId);
        return eventService.updateEventByAdmin(eventId, newEventDto);
    }

    @GetMapping("/events")
    public List<EventFullDto> getEventsForAdmin(@RequestParam(name = "users", required = false) List<Long> users,
                                                @RequestParam(name = "states",
                                                        required = false) List<EventState> states,
                                                @RequestParam(name = "categories",
                                                        required = false) List<Long> categories,
                                                @RequestParam(name = "rangeStart",
                                                        defaultValue = "1950-01-01 13:30:38")
                                                String rangeStart,
                                                @RequestParam(name = "rangeEnd",
                                                        defaultValue = "#{T(java.time.LocalDateTime).now()" +
                                                                ".format(T(java.time.format.DateTimeFormatter)" +
                                                                ".ofPattern('yyyy-MM-dd HH:mm:ss'))}")
                                                String rangeEnd,
                                                @RequestParam(name = "from",
                                                        defaultValue = "0") @PositiveOrZero Integer from,
                                                @RequestParam(name = "size",
                                                        defaultValue = "10") @Positive Integer size) {
        log.info("Get events by admin users={}, states={}, categories={}, rangeStart={}, rangeEnd={}," +
                "from={}, size={}", users, states, categories, rangeStart, rangeEnd, from, size);
        return eventService.getEventsForAdmin(users, states, categories, rangeStart, rangeEnd, from, size);
    }
}

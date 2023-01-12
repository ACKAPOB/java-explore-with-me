package ewmservice.controllers.common;

import ewmservice.event.dto.EventFullDto;
import ewmservice.event.dto.EventShortDto;
import ewmservice.event.service.EventServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequestMapping
@Slf4j
public class PublicEventController {
    private final EventServiceImpl eventService;

    public PublicEventController(EventServiceImpl eventService) {
        this.eventService = eventService;
    }

    @GetMapping("/events")
    public List<EventShortDto> getEventsForUser(@RequestParam(name = "text", required = false) String text,
                                                @RequestParam(name = "categories",
                                                        required = false) List<Long> categories,
                                                @RequestParam(name = "paid", required = false) Boolean paid,
                                                @RequestParam(name = "rangeStart",
                                                        defaultValue = "1950-01-01 13:30:38") String rangeStart,
                                                @RequestParam(name = "rangeEnd",
                                                        defaultValue = "2090-01-01 00:00:00") String rangeEnd,
                                                @RequestParam(name = "from",
                                                        defaultValue = "0") @PositiveOrZero Integer from,
                                                @RequestParam(name = "size",
                                                        defaultValue = "10") @Positive Integer size,
                                                HttpServletRequest request) {
        log.info("Get events with much requirements text={}, categories={}, paid={}, rangeStart={}, " +
                        "rangeEnd={}, from={}, size={}", text, categories, paid,
                rangeStart, rangeEnd, from, size);
        log.info("client ip: {}", request.getRemoteAddr());
        log.info("endpoint path: {}", request.getRequestURI());
        return eventService.getEventsForUser(text, categories, paid, rangeStart, rangeEnd, from, size, request);
    }

    @GetMapping("/events/{id}")
    public EventFullDto getEventById(@PathVariable Long id, HttpServletRequest request) {
        log.info("client ip: {}", request.getRemoteAddr());
        log.info("endpoint path: {}", request.getRequestURI());
        return eventService.getEventById(id, request);
    }
}

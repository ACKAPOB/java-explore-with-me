package ru.practicum.explore.controllers.publicController;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore.event.dto.EventFullDto;
import ru.practicum.explore.event.dto.EventShortDto;
import ru.practicum.explore.event.service.EventService;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/events")
@Slf4j
public class PublicEventController {
    private final EventService eventService;

    @Autowired
    public PublicEventController(EventService eventService) {
        this.eventService = eventService;
    }

    @GetMapping
    public Collection<EventShortDto> getAllEvent(@RequestParam String text,
                                            @RequestParam List<Long> categories,
                                            @RequestParam Boolean paid,
                                            @RequestParam String rangeStart,
                                            @RequestParam String rangeEnd,
                                            @RequestParam Boolean onlyAvailable,
                                            @RequestParam String sort,
                                            @RequestParam(defaultValue = "0") Integer from,
                                            @RequestParam(defaultValue = "10") Integer size,
                                            HttpServletRequest request) {
        Map<String, Object> parameters = Map.of(
                "text", text,
                "categories", categories,
                "paid", paid,
                "rangeStart", rangeStart,
                "rangeEnd", rangeEnd,
                "onlyAvailable", onlyAvailable,
                "sort", sort,
                "from", from,
                "size", size
        );
        log.info("Получение событий с возможностью фильтрации PublicEventController.getAllEvent {}", parameters);
        eventService.saveInStatService(request);
        return eventService.getAllEvent(parameters);
    }

    @GetMapping("/{eventId}")
    public Optional<EventFullDto> getEvent(@PathVariable Long eventId, HttpServletRequest request) {
        eventService.saveInStatService(request);
        log.info("Получение подробной информации об опубликованном событии по его идентификатору " +
                "PublicEventController.getEvent id={}", eventId);
        return eventService.getEvent(eventId);
    }
}




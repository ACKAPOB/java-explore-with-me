package ru.practicum.ewm.controller.Public;

import lombok.RequiredArgsConstructor;
import ru.practicum.ewm.event.dto.EventFullDto;
import ru.practicum.ewm.event.dto.EventShortDto;
import ru.practicum.ewm.event.service.EventService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/events")
public class PublicEventController {
    private final EventService eventService;

    @GetMapping
    public List<EventShortDto> getSortedEvents(
            @RequestParam(name = "text", required = false) String text,
            @RequestParam(name = "categories", required = false) List<Long> categories,
            @RequestParam(name = "paid", required = false) Boolean paid,
            @RequestParam(name = "rangeStart", defaultValue = "1950-01-01 13:30:38") String rangeStart,
            @RequestParam(name = "rangeEnd", defaultValue = "2090-01-01 00:00:00") String rangeEnd,
            @RequestParam(name = "from", defaultValue = "0") @PositiveOrZero Integer from,
            @RequestParam(name = "size", defaultValue = "10") @Positive Integer size, HttpServletRequest request) {
        log.info("Получение событий с возможностью фильтрации PublicEventController.getSortedEvents text={}, " +
                        "categories={}, paid={}, rangeStart={}, rangeEnd={}, from={}, size={}, request = {}",
                text, categories, paid, rangeStart, rangeEnd, from, size, request);
        return eventService.getSortedEvents(text, categories, paid, rangeStart, rangeEnd, from, size, request);
    }

    @GetMapping("/{eventId}")
    public EventFullDto getEventById(@PathVariable Long eventId, HttpServletRequest request) {
        log.info("Получение подробной информации об опубликованном событии по его идентификатору " +
                "PublicEventController.getEventById, eventId = {}, request = {}", eventId, request);
        return eventService.getEventById(eventId, request);
    }
}

package ru.practicum.explore.controllers.publicController;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore.event.dto.EventFullDto;
import ru.practicum.explore.event.dto.EventShortDto;
import ru.practicum.explore.event.model.EventSort;
import ru.practicum.explore.event.service.impl.PublicEventServiceImpl;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/events")
@Slf4j
public class PublicEventController {
    private final PublicEventServiceImpl publicEventService;

    @Autowired
    public PublicEventController(PublicEventServiceImpl publicEventService) {
        this.publicEventService = publicEventService;
    }

    @GetMapping
    public List<EventShortDto> getAllEvent(
                            @RequestParam(required = false) String text,
                            @RequestParam(required = false) List<Long> categories,
                            @RequestParam(required = false) Boolean paid,
                            @RequestParam(required = false)
                                            @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeStart,
                            @RequestParam(required = false)
                                            @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeEnd,
                            @RequestParam(required = false) Boolean onlyAvailable,
                            @RequestParam(required = false, defaultValue = "EVENT_DATE") EventSort sort,
                            @RequestParam(defaultValue = "0") Integer from,
                            @RequestParam(defaultValue = "10") Integer size,
            HttpServletRequest request) {
        log.info("Получение событий с возможностью фильтрации PublicEventController.getAllEvent text = {}", text);
        return publicEventService.getAllEvent(text, categories, paid, rangeStart, rangeEnd, onlyAvailable, sort, from, size, request);
    }

    @GetMapping("/{eventId}")
    public Optional<EventFullDto> getEvent(@PathVariable Long eventId, HttpServletRequest request) {
        log.info("Получение подробной информации об опубликованном событии по его идентификатору " +
                "PublicEventController.getEvent id={}", eventId);
        return publicEventService.getEvent(eventId, request);
    }
}




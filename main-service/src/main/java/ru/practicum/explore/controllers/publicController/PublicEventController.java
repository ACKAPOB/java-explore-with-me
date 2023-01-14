package ru.practicum.explore.controllers.publicController;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore.event.dto.EventFullDto;
import ru.practicum.explore.event.dto.EventShortDto;
import ru.practicum.explore.event.service.impl.PublicEventServiceImpl;

import javax.servlet.http.HttpServletRequest;
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
    public List<EventShortDto> getAllEvent(@RequestParam String text,
                                            @RequestParam List<Long> categories,
                                            @RequestParam Boolean paid,
                                            @RequestParam String rangeStart,
                                            @RequestParam String rangeEnd,
                                            @RequestParam Boolean onlyAvailable,
                                            @RequestParam String sort,
                                            @RequestParam(defaultValue = "0") Integer from,
                                            @RequestParam(defaultValue = "10") Integer size,
                                            HttpServletRequest request) {
        log.info("Получение событий с возможностью фильтрации PublicEventController.getAllEvent text = {}", text);
        publicEventService.saveInStatService(request);
        return publicEventService.getAllEvent(text, categories, paid, rangeStart, rangeEnd, onlyAvailable, sort, from, size);
    }

    @GetMapping("/{eventId}")
    public Optional<EventFullDto> getEvent(@PathVariable Long eventId, HttpServletRequest request) {
        publicEventService.saveInStatService(request);
        log.info("Получение подробной информации об опубликованном событии по его идентификатору " +
                "PublicEventController.getEvent id={}", eventId);
        return publicEventService.getEvent(eventId);
    }
}




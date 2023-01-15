package ru.practicum.explore.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore.dto.ViewStatsDto;
import ru.practicum.explore.dto.EndpointHitDto;
import ru.practicum.explore.service.StatsService;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/")
@Slf4j
public class StatsController {
    private final StatsService service;

    @PostMapping("hit")
    public void save(@RequestBody EndpointHitDto endpointHitDto) {
        log.info("Получен запрос StatsController.save");
        service.save(endpointHitDto);
    }

    @GetMapping("stats")
    public List<ViewStatsDto> getViews(@RequestParam String start,
                                       @RequestParam String end,
                                       @RequestParam(defaultValue = "false") Boolean unique,
                                       @RequestParam(required = false) List<String> uris) {
        log.info("Получен запрос StatsController.getViews");
        return service.getViews(start, end, unique, uris);
    }
}
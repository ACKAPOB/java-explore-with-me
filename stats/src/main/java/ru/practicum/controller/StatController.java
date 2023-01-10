package ru.practicum.controller;

import lombok.RequiredArgsConstructor;
import ru.practicum.stats.dto.ShortStatDto;
import ru.practicum.stats.dto.StatDto;
import ru.practicum.stats.service.StatService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
public class StatController {
    private final StatService service;

    @PostMapping("/hit")
    public StatDto saveStatistic(@RequestBody StatDto statDto) {
        log.info("Creating hit={}", statDto);
        return service.saveStatistic(statDto);
    }

    @GetMapping("/stats")
    public List<ShortStatDto> getStatistic(@RequestParam(name = "uris", required = false) List<String> uris,
                                           @RequestParam(name = "start",
                                                   defaultValue = "1950-01-01 00:00:00") String start,
                                           @RequestParam(name = "end",
                                                   defaultValue = "2090-01-01 00:00:00") String end,
                                           @RequestParam(name = "unique", defaultValue = "false") Boolean unique) {
        log.info("Get hits uris={}, start={}, end={}, unique={}", uris, start, end, unique);
        return service.getStatistic(uris, start, end, unique);
    }
}

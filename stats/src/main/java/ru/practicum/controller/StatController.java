package ru.practicum.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.EndpointHit;
import ru.practicum.dto.ViewStatsDto;
import ru.practicum.service.StatService;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
public class StatController {
    private final StatService statService;

    @GetMapping("/stats")
    public List<ViewStatsDto> getStatistics(@RequestParam String start,
                                            @RequestParam String end,
                                            @RequestParam(required = false) List<String> uris,
                                            @RequestParam(defaultValue = "false") boolean unique) {
        log.info("Get statistic by params: start {}, end {}, uris {}, unique {}",start, end, uris, unique);
        return statService.getStatistic(start, end, uris, unique);
    }

    @PostMapping("/hit")
    public void createHit(@RequestBody EndpointHit endpointHit) {
        log.info("Create statHit {}", endpointHit);
        statService.createHit(endpointHit);
    }
}

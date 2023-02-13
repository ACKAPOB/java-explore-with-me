package ru.practicum.explore.mapper;


import lombok.extern.slf4j.Slf4j;
import ru.practicum.explore.dto.EndpointHitDto;
import ru.practicum.explore.dto.ViewStatsDto;
import ru.practicum.explore.model.EndpointHit;
import ru.practicum.explore.model.ViewStats;

import java.time.LocalDateTime;

@Slf4j
public final class StatsMapper {

    public static EndpointHit toEndpointHit(EndpointHitDto endpointHitDto) {
        log.info("Отработал StatsMapper.toEndpointHit");
        return EndpointHit.builder()
                .app(endpointHitDto.getApp())
                .ip(endpointHitDto.getIp())
                .uri(endpointHitDto.getUri())
                .timestamp(LocalDateTime.now())
                .build();
    }

    public static ViewStatsDto toViewStatsDto(ViewStats viewStats) {
        log.info("Отработал StatsMapper.toViewStatsDto");
        return  ViewStatsDto.builder()
                .app(viewStats.getApp())
                .uri(viewStats.getUri())
                .hits(viewStats.getHits())
                .build();
    }
}
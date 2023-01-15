package ru.practicum.explore.service;

import org.springframework.stereotype.Service;
import ru.practicum.explore.dto.ViewStatsDto;
import ru.practicum.explore.dto.EndpointHitDto;

import java.util.List;

@Service
public interface StatsService {

    void save(EndpointHitDto endpointHitDto);

    List<ViewStatsDto> getViews(String start, String end, Boolean unique, List<String> uris);
}

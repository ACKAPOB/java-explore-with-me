package ru.practicum.explore.service;

import ru.practicum.explore.dto.EndpointHit;
import ru.practicum.explore.dto.ViewStats;

import java.util.Collection;
import java.util.List;

public interface StatsService {

    EndpointHit save(EndpointHit endpointHit);

    Collection<ViewStats> getStats(String start, String end, List<String> uris, Boolean unique);
}

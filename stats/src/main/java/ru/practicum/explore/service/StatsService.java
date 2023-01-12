package ru.practicum.explore.service;

import ru.practicum.explore.dto.EndpointHit;
import ru.practicum.explore.dto.ViewStats;

import java.util.Collection;
import java.util.List;

/**
 * Интерфейс сервиса статистики
 */
public interface StatsService {
    /*
    Сохранение статистики
    */
    EndpointHit save(EndpointHit endpointHit);

    /*
    Молучение статистики
    */
    Collection<ViewStats> getStats(String start, String end, List<String> uris, Boolean unique);
}

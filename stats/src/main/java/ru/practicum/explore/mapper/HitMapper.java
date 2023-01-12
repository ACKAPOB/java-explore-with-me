package ru.practicum.explore.mapper;

import ru.practicum.explore.dto.EndpointHit;
import ru.practicum.explore.dto.ViewStats;
import ru.practicum.explore.model.Hit;

import java.util.List;

/**
 * Интерфейс маппера статистики
 */
public interface HitMapper {
    /*
    Метод маппера для получения модели
    */
    Hit toHit(EndpointHit endpointHit);

    /*
    Метод маппера для получения из модели в dto
    */
    EndpointHit toEndpointHit(Hit hit);

    /*
    Метод маппера получения статистики
    */
    ViewStats toViewStats(List<Hit> list);
}

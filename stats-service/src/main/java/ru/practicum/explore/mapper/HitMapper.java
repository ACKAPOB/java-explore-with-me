package ru.practicum.explore.mapper;

import ru.practicum.explore.dto.EndpointHit;
import ru.practicum.explore.dto.ViewStats;
import ru.practicum.explore.model.Hit;

import java.util.List;

public interface HitMapper {

    Hit toHit(EndpointHit endpointHit);

    EndpointHit toEndpointHit(Hit hit);

    ViewStats toViewStats(List<Hit> list);
}

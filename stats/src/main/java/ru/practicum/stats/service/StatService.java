package ru.practicum.stats.service;

import ru.practicum.stats.dto.ShortStatDto;
import ru.practicum.stats.dto.StatDto;

import java.util.List;

public interface StatService {
    StatDto saveStatistic(StatDto statDto);

    List<ShortStatDto> getStatistic(List<String> uris, String start,
                                    String end, Boolean unique);
}

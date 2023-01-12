package ru.practicum.explore.service;

import org.springframework.stereotype.Service;
import ru.practicum.explore.dto.EndpointHit;
import ru.practicum.explore.dto.ViewStats;
import ru.practicum.explore.mapper.HitMapper;
import ru.practicum.explore.model.Hit;
import ru.practicum.explore.repository.StatsRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class StatsServiceImpl implements StatsService {
    private final StatsRepository statsRepository;
    private final HitMapper hitMapper;

    public StatsServiceImpl(StatsRepository statsRepository, HitMapper hitMapper) {
        this.statsRepository = statsRepository;
        this.hitMapper = hitMapper;
    }

    @Override
    public EndpointHit save(EndpointHit endpointHit) {
        Hit hit = hitMapper.toHit(endpointHit);
        return hitMapper.toEndpointHit(statsRepository.save(hit));
    }

    @Override
    public Collection<ViewStats> getStats(String start, String end, List<String> uris, Boolean unique) {
        LocalDateTime startTime = LocalDateTime.parse(start, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        LocalDateTime endTime = LocalDateTime.parse(end, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        Collection<ViewStats> list = new ArrayList<>();
        if (uris == null) {
            uris = statsRepository.findAllByTime(startTime, endTime).stream().distinct().collect(Collectors.toList());
        }
        List<Hit> hits;
        for (String uri : uris) {
            if (uri.contains("[")) {
                hits = statsRepository.findAllByUri(uri.substring(1, uri.length() - 1), startTime, endTime);
            } else {
                hits = statsRepository.findAllByUri(uri, startTime, endTime);
            }
            if (hits.size() > 0) {
                list.add(hitMapper.toViewStats(hits));
            }
        }
        return list;
    }
}

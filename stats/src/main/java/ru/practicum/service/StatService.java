package ru.practicum.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.dto.EndpointHit;
import ru.practicum.dto.ViewStatsDto;
import ru.practicum.mapper.StatMapper;
import ru.practicum.model.Statistic;
import ru.practicum.repository.StatRepository;


import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class StatService {
    private final StatRepository statRepository;

    @Transactional
    public void createHit(EndpointHit endpointHit) {
        Statistic statistic = StatMapper.toStatistic(endpointHit);
        statRepository.save(statistic);
    }

    public List<ViewStatsDto> getStatistic(String start, String end, List<String> uris, boolean unique) {
        LocalDateTime startDate = toLocalDateTime(start);
        LocalDateTime endDate = toLocalDateTime(end);

        if (unique) {
            return statRepository.getUniqueViews(startDate, endDate, uris).stream()
                    .map(StatMapper::toViewStats)
                    .collect(Collectors.toList());
        } else {
            return statRepository.getViews(startDate, endDate, uris).stream()
                    .map(StatMapper::toViewStats)
                    .collect(Collectors.toList());
        }
    }

    private LocalDateTime toLocalDateTime(String date) {
        String decodeDate = URLDecoder.decode(date, StandardCharsets.UTF_8);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return LocalDateTime.parse(decodeDate, formatter);
    }
}

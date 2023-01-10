package ru.practicum.stats.mapper;

import ru.practicum.stats.dto.ShortStatDto;
import ru.practicum.stats.dto.StatDto;
import ru.practicum.stats.model.Stat;
import org.springframework.stereotype.Component;

@Component
public class StatMapper {


    public static Stat toStat(StatDto statDto) {
        return Stat.builder()
                .id(statDto.getId())
                .app(statDto.getApp())
                .uri(statDto.getUri())
                .ip(statDto.getIp())
                .timestamp(statDto.getTimestamp())
                .build();
    }

    public static StatDto toStatDto(Stat stat) {
        return StatDto.builder()
                .id(stat.getId())
                .app(stat.getApp())
                .uri(stat.getUri())
                .ip(stat.getIp())
                .timestamp(stat.getTimestamp())
                .build();
    }

    public static ShortStatDto toShortStatDto(Stat stat) {
        return ShortStatDto.builder()
                .app(stat.getApp())
                .uri(stat.getUri())
                .build();
    }
}

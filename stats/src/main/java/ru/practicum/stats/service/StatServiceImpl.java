package ru.practicum.stats.service;

import ru.practicum.stats.dto.ShortStatDto;
import ru.practicum.stats.dto.StatDto;
import ru.practicum.stats.mapper.StatMapper;
import ru.practicum.stats.model.Stat;
import ru.practicum.stats.repository.StatRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.stats.mapper.StatMapper.toStat;
import static ru.practicum.stats.mapper.StatMapper.toStatDto;


@Service
public class StatServiceImpl implements StatService {
    private final StatRepository statRepository;

    public StatServiceImpl(StatRepository statRepository) {
        this.statRepository = statRepository;
    }

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public StatDto saveStatistic(StatDto statDto) {
        String s = statDto.getUri();
        s = s.replace("[", "");
        s = s.replace("]", "");
        statDto.setHits(0L);
        statDto.setUri(s);
        return toStatDto(statRepository.save(toStat(statDto)));
    }

    @Override
    public List<ShortStatDto> getStatistic(List<String> uris, String start, String end, Boolean unique) {
        List<Stat> stats;
        if (!unique) {
            stats = statRepository.findAllByUris(uris, LocalDateTime.parse(start, formatter),
                    LocalDateTime.parse(end, formatter));
        } else {
            stats = statRepository.findAllByUniqUris(uris, LocalDateTime.parse(start, formatter),
                    LocalDateTime.parse(end, formatter));
        }
        List<ShortStatDto> shortStatDtos = stats.stream()
                .map(StatMapper::toShortStatDto)
                .collect(Collectors.toList());
        shortStatDtos
                .forEach(shortStatDto -> shortStatDto.setHits(statRepository.countByUri(shortStatDto.getUri())));
        return shortStatDtos;
    }
}

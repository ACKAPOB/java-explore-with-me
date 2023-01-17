package ru.practicum.explore.compilation.service.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.explore.compilation.dto.CompilationDto;
import ru.practicum.explore.compilation.mapper.CompilationMapper;
import ru.practicum.explore.compilation.model.Compilation;
import ru.practicum.explore.compilation.repository.CompilationRepository;
import ru.practicum.explore.compilation.service.PublicCompilationService;
import ru.practicum.explore.event.dto.EventShortDto;
import ru.practicum.explore.event.mapper.EventMapper;
import ru.practicum.explore.exception.ObjectNotFoundException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@AllArgsConstructor
public class PublicCompilationServiceImpl implements PublicCompilationService {
    private final CompilationRepository compilationRepository;
    private final EventMapper eventMapper;

    @Override
    public List<CompilationDto> getCompilationAll(Boolean pinned, Integer from, Integer size) {
        log.info("Получение подборок событий PublicCompilationServiceImpl.getCompilationAll");
        Collection<Compilation> compilationCollection =
                compilationRepository.findAllByPinned(pinned, PageRequest.of(from / size, size));
        List<CompilationDto> compilationDto = new ArrayList<>();
        if (!compilationCollection.isEmpty()) {
            for (Compilation compilation : compilationCollection) {
                List<EventShortDto> eventShortDtoList = new ArrayList<>();
                if (compilation.getEvents().size() != 0) {
                    eventShortDtoList = compilation
                            .getEvents()
                            .stream()
                            .map(eventMapper::toEventShortDto)
                            .collect(Collectors.toList());
                }
                compilationDto.add(CompilationMapper.toCompilationDto(compilation, eventShortDtoList));
            }
        }
        return compilationDto;
    }

    @Override
    public Optional<CompilationDto> getCompilation(Long compId) {
        log.info("Получение подборки событий по его id PublicCompilationServiceImpl.getCompilation compId={}", compId);
        Compilation compilation = compilationRepository
                .findById(compId)
                .orElseThrow(() -> new ObjectNotFoundException(String.format("Compilation not found id = %s", compId)));
        List<EventShortDto> eventShortDtoList = new ArrayList<>();
        if (compilation.getEvents().size() != 0) {
            eventShortDtoList = compilation
                    .getEvents()
                    .stream()
                    .map(eventMapper::toEventShortDto)
                    .collect(Collectors.toList());
        }
        return Optional.of(CompilationMapper.toCompilationDto(compilation, eventShortDtoList));
    }
}

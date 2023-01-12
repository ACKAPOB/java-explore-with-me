package ru.practicum.explore.compilation.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.explore.compilation.dto.CompilationDto;
import ru.practicum.explore.compilation.service.CompilationService;
import ru.practicum.explore.event.dto.EventShortDto;
import ru.practicum.explore.exception.errors.validate.ObjectValidate;
import ru.practicum.explore.compilation.mapper.CompilationMapper;
import ru.practicum.explore.event.mapper.EventMapper;
import ru.practicum.explore.compilation.model.Compilation;
import ru.practicum.explore.compilation.repository.CompilationRepository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class CompilationServiceImpl implements CompilationService {
    private final CompilationRepository compilationRepository;
    private final CompilationMapper compilationMapper;
    private final EventMapper eventMapper;
    private final ObjectValidate objectValidate;

    @Autowired
    public CompilationServiceImpl(CompilationRepository compilationRepository, CompilationMapper compilationMapper,
                                  EventMapper eventMapper, ObjectValidate objectValidate) {
        this.compilationRepository = compilationRepository;
        this.compilationMapper = compilationMapper;
        this.eventMapper = eventMapper;
        this.objectValidate = objectValidate;
    }

    @Override
    public Collection<CompilationDto> getCompilationAll(Boolean pinned, Integer from, Integer size) {
        log.info("Получение подборок событий CompilationServiceImpl.getAll");
        Pageable pageable = PageRequest.of(from / size, size);
        Collection<Compilation> compilationCollection =
                compilationRepository.findAllByPinnedOrderById(pinned, pageable);
        Collection<CompilationDto> compilationDtoCollection = new ArrayList<>();
        if (!compilationCollection.isEmpty()) {
            for (Compilation c : compilationCollection) {
                List<EventShortDto> eventShortDtoList = new ArrayList<>();
                if (c.getEvents().size() != 0) {
                    eventShortDtoList = c.getEvents().stream()
                            .map(eventMapper::toEventShortDto)
                            .collect(Collectors.toList());
                }
                compilationDtoCollection.add(compilationMapper.toCompilationDto(c, eventShortDtoList));
            }
        }
        return compilationDtoCollection;
    }

    @Override
    public Optional<CompilationDto> getCompilation(Long compId) {
        log.info("Получение подборки событий по его id CompilationServiceImpl.getCompilation compId={}", compId);
        objectValidate.validateCompilation(compId);
        Compilation compilation = compilationRepository.findById(compId).get();
        List<EventShortDto> eventShortDtoList = new ArrayList<>();
        if (compilation.getEvents().size() != 0) {
            eventShortDtoList = compilation.getEvents().stream()
                    .map(eventMapper::toEventShortDto)
                    .collect(Collectors.toList());
        }
        return Optional.of(compilationMapper.toCompilationDto(compilation, eventShortDtoList));
    }
}

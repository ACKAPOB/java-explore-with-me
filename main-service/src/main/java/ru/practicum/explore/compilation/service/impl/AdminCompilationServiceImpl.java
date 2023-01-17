package ru.practicum.explore.compilation.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.explore.compilation.dto.CompilationDto;
import ru.practicum.explore.compilation.dto.NewCompilationDto;
import ru.practicum.explore.compilation.mapper.CompilationMapper;
import ru.practicum.explore.compilation.model.Compilation;
import ru.practicum.explore.compilation.repository.CompilationRepository;
import ru.practicum.explore.compilation.service.AdminCompilationService;
import ru.practicum.explore.event.dto.EventShortDto;
import ru.practicum.explore.event.mapper.EventMapper;
import ru.practicum.explore.event.model.Event;
import ru.practicum.explore.event.repository.EventRepository;
import ru.practicum.explore.exception.ObjectNotFoundException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class AdminCompilationServiceImpl implements AdminCompilationService {

    private final CompilationRepository compilationRepository;
    private final EventMapper eventMapper;
    private final EventRepository eventRepository;

    public AdminCompilationServiceImpl(CompilationRepository compilationRepository,
                                       EventMapper eventMapper,
                                       EventRepository eventRepository) {
        this.compilationRepository = compilationRepository;
        this.eventMapper = eventMapper;
        this.eventRepository = eventRepository;
    }

    @Override
    public CompilationDto createCompilation(NewCompilationDto newCompilationDto) {
        log.info("обавление новой подборки newCompilationDto = {} " +
                "AdminCompilationServiceImpl. createCompilation",newCompilationDto);
        List<Event> events = eventRepository.findAllById(newCompilationDto.getEvents());
        Compilation compilation = CompilationMapper.toCompilation(newCompilationDto, events);
        compilationRepository.save(compilation);
        List<EventShortDto> eventShortDtoList = events
                .stream()
                .map(eventMapper::toEventShortDto)
                .collect(Collectors.toList());
        return CompilationMapper.toCompilationDto(compilation, eventShortDtoList);
    }

    @Override
    public void deleteCompilation(Long compId) {
        log.info("Удаление подборки id = {} AdminCompilationServiceImpl.deleteCompilation", compId);
        compilationRepository
                .delete(compilationRepository.findById(compId)
                .orElseThrow(() -> new ObjectNotFoundException(String.format("Compilation not found id = %s", compId))));
    }

    @Override
    public void deleteEventInCompilation(Long compId, Long eventId) {
        log.info("Удалить событие из подборки event eventId = {}, compId = {} " +
                "AdminCompilationServiceImpl.deleteEventInCompilation", eventId, compId);
        Compilation compilation = compilationRepository
                .findById(compId)
                .orElseThrow(() -> new ObjectNotFoundException(String.format("Compilation not found id = %s", compId)));
        Event event = eventRepository
                .findById(eventId)
                .orElseThrow(() -> new ObjectNotFoundException(String.format("Event not found id = %s", eventId)));
        if (!compilation.getEvents().contains(event)) {
            return;
        }
        compilation.getEvents().remove(event);
        compilationRepository.save(compilation);
    }

    @Override
    public void addEventInCompilation(Long compId, Long eventId) {
        log.info("Добавить событие в подборку eventId = {} in compId id = {} " +
                "AdminCompilationServiceImpl.addEventInCompilation", eventId, compId);
        Compilation compilation = compilationRepository
                .findById(compId)
                .orElseThrow(() -> new ObjectNotFoundException(String.format("Compilation not found id = %s", compId)));
        Event event = eventRepository
                .findById(eventId)
                .orElseThrow(() -> new ObjectNotFoundException(String.format("Event not found id = %s", eventId)));
        if (compilation.getEvents().contains(event)) {
            return;
        }
        compilation.getEvents().add(event);
        compilationRepository.save(compilation);
    }

    @Override
    public void unpinCompilation(Long compId) {
        log.info("Открепить подборку на главной странице compId = {} " +
                "AdminCompilationServiceImpl.unpinCompilation", compId);
        Compilation compilation = compilationRepository
                .findById(compId)
                .orElseThrow(() -> new ObjectNotFoundException(String.format("Compilation not found id = %s", compId)));
        //Запихнуть бы надо setPinned(false) после  .orElseThrow(() но ругается на воид метод
        compilation.setPinned(false);
        compilationRepository.save(compilation);
    }

    @Override
    public void pinCompilation(Long compId) {
        log.info("Закрепить подборку на главной странице compId = {} " +
                "AdminCompilationServiceImpl.pinCompilation", compId);
        Compilation compilation = compilationRepository
                .findById(compId)
                .orElseThrow(() -> new ObjectNotFoundException(String.format("Compilation not found id = %s", compId)));
        compilation.setPinned(true);
        compilationRepository.save(compilation);
    }

}

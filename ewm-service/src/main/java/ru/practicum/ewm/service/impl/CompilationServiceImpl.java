package ru.practicum.ewm.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.dto.CompilationDto;
import ru.practicum.ewm.dto.NewCompilationDto;
import ru.practicum.ewm.model.Compilation;
import ru.practicum.ewm.mapper.CompilationMapper;
import ru.practicum.ewm.repository.CompilationRepository;
import ru.practicum.ewm.model.Event;
import ru.practicum.ewm.repository.EventRepository;
import ru.practicum.ewm.exception.NotFoundException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.service.CompilationService;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static ru.practicum.ewm.mapper.CompilationMapper.*;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class CompilationServiceImpl implements CompilationService {
    private final CompilationRepository compilationRepository;
    private final EventRepository eventRepository;

    @Override
    @Transactional
    public CompilationDto createCompilation(NewCompilationDto newCompilationDto) {
        Set<Event> events = new HashSet<>(eventRepository.findAllById(newCompilationDto.getEvents()));
        Compilation compilation = compilationRepository.save(toCompilationFromNew(newCompilationDto, events));
        compilation.setEvents(events);
        log.info("Создание подборки событий CompilationServiceImpl.createCompilation, newCompilationDto = {}",
                newCompilationDto);
        return toCompilationDto(compilation);
    }

    @Override
    @Transactional
    public void deleteEventFromCompilation(Long compId, Long eventId) {
        log.info("Удаление события из подборки CompilationServiceImpl.deleteEventFromCompilation id = {}, " +
                "eventId = {}", compId, eventId);
        Compilation compilation = compilationRepository.findById(compId)
                .orElseThrow(() -> new NotFoundException(String.format("Нет подборки с id %d", compId)));
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException(String.format("События с id %d не существует", eventId)));
        Set<Event> events = compilation.getEvents();
        events.remove(event);
        compilation.setEvents(events);
        compilationRepository.save(compilation);
    }

    @Override
    @Transactional
    public CompilationDto getCompilationById(Long compId) {
        log.info("Получение подборки событий по id = {}, CompilationServiceImpl.getCompilationById", compId);
        return compilationRepository
                .findById(compId)
                .map(CompilationMapper::toCompilationDto)
                .orElseThrow(() -> new NotFoundException(String.format("Нет подборки с id %d", compId)));
    }

    @Override
    @Transactional
    //Получение подборок событий
    public List<CompilationDto> getCompilations(Boolean pinned, Integer from, Integer size) {
        log.info("Получение подборок событий CompilationsServiceImpl.getRequiredCompilations, pinned = {}, " +
                "from = {}, size = {}", pinned, from, size);
        return compilationRepository.findAllByPinnedIsTrue(pinned,
                        PageRequest.of(from / size, size, Sort.by("id").descending()))
                .stream()
                .map(CompilationMapper::toCompilationDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public NewCompilationDto addEventToCompilation(Long compId, Long eventId) {
        log.info("Добавление события в компиляцию CompilationsServiceImpl.addEventToCompilation, compId = {}, " +
                "eventId = {}", compId, eventId);
        Compilation compilation = compilationRepository.findById(compId)
                .orElseThrow(() -> new NotFoundException(String.format("Нет подборки с id %d", compId)));
        Set<Event> events = compilation.getEvents();
        events.add(eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException(String.format("События с id %d не существует", eventId))));
        compilation.setEvents(events);
        compilationRepository.save(compilation);
        return toNewCompilationDto(compilation);
    }

    @Override
    @Transactional
    public void deletePinCompilation(Long compId) {
        log.info("Открепить подборку на главной странице CompilationsServiceImpl.deletePinCompilation id = {}", compId);
        Compilation compilation = compilationRepository.findById(compId)
                .orElseThrow(() -> new NotFoundException(String.format("Нет подборки с id %d", compId)));
        compilation.setPinned(false);
        compilationRepository.save(compilation);
    }

    @Override
    @Transactional
    public void pinCompilation(Long compId) {
        log.info("Прикрепить подборку на главной странице CompilationsServiceImpl.pinCompilation id = {}", compId);
        Compilation compilation = compilationRepository.findById(compId)
                .orElseThrow(() -> new NotFoundException(String.format("Нет подборки с id %d", compId)));
        compilation.setPinned(true);
        compilationRepository.save(compilation);
    }

    @Override
    @Transactional
    public void deleteCompilation(Long compId) {
        log.info("Удаление подборки событий CompilationsServiceImpl.deleteCompilation id = {}", compId);
        compilationRepository.deleteById(compId);
    }
}

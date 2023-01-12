package ewmservice.compilation.service;

import ewmservice.compilation.dto.CompilationDto;
import ewmservice.compilation.dto.NewCompilationDto;
import ewmservice.compilation.model.Compilation;
import ewmservice.compilation.repository.CompilationRepository;
import ewmservice.event.model.Event;
import ewmservice.event.repository.EventRepository;
import ewmservice.exception.EntityNotFoundException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static ewmservice.compilation.mapper.CompilationMapper.*;

@Service
public class CompilationServiceImpl implements CompilationService {
    private final CompilationRepository compilationRepository;
    private final EventRepository eventRepository;

    public CompilationServiceImpl(CompilationRepository compilationRepository, EventRepository eventRepository) {
        this.compilationRepository = compilationRepository;
        this.eventRepository = eventRepository;
    }

    @Override
    public CompilationDto saveCompilation(NewCompilationDto newCompilationDto) {
        Set<Event> events = new HashSet<>(eventRepository.findAllById(newCompilationDto.getEvents()));
        Compilation compilation = compilationRepository.save(toCompilationFromNew(newCompilationDto, events));
        compilation.setEvents(events);
        return toCompilationDto(compilation);
    }

    @Override
    public void deleteEventFromCompilation(Long compId, Long eventId) {
        Compilation compilation = compilationRepository.findById(compId)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Нет подборки с id %d", compId)));
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() ->
                        new EntityNotFoundException(String.format("События с id %d не существует", eventId)));
        Set<Event> events = compilation.getEvents();
        events.remove(event);
        compilation.setEvents(events);
        compilationRepository.save(compilation);
    }

    @Override
    public CompilationDto getCompilationById(Long compId) {
        Compilation compilation = compilationRepository.findById(compId)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Нет подборки с id %d", compId)));
        return toCompilationDto(compilation);
    }

    @Override
    public List<CompilationDto> getRequiredCompilations(Boolean pinned, Integer from, Integer size) {
        int page = from / size;
        Pageable pageRequest = PageRequest.of(page, size, Sort.by("id").descending());
        List<Compilation> compilations = compilationRepository.findAllByPinnedIsTrue(pinned, pageRequest);
        List<CompilationDto> compilationDtos = new ArrayList<>();
        CompilationDto compilationDto;
        for (Compilation compilation : compilations) {
            compilationDto = toCompilationDto(compilation);
            compilationDtos.add(compilationDto);
        }
        return compilationDtos;
    }

    @Override
    public NewCompilationDto addEventToCompilation(Long compId, Long eventId) {
        Compilation compilation = compilationRepository.findById(compId)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Нет подборки с id %d", compId)));
        Set<Event> events = compilation.getEvents();
        events.add(eventRepository.findById(eventId)
                .orElseThrow(() ->
                        new EntityNotFoundException(String.format("События с id %d не существует", eventId))));
        compilation.setEvents(events);
        compilationRepository.save(compilation);
        return toNewCompilationDto(compilation);
    }

    @Override
    public void unPinCompilation(Long compId) {
        Compilation compilation = compilationRepository.findById(compId)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Нет подборки с id %d", compId)));
        compilation.setPinned(false);
        compilationRepository.save(compilation);
    }

    @Override
    public void pinCompilation(Long compId) {
        Compilation compilation = compilationRepository.findById(compId)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Нет подборки с id %d", compId)));
        compilation.setPinned(true);
        compilationRepository.save(compilation);
    }

    @Override
    public void deleteCompilation(Long compId) {
        compilationRepository.deleteById(compId);
    }
}

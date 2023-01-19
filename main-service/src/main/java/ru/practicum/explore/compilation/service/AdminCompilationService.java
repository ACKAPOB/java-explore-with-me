package ru.practicum.explore.compilation.service;

import ru.practicum.explore.compilation.dto.CompilationDto;
import ru.practicum.explore.compilation.dto.NewCompilationDto;

public interface AdminCompilationService {

    CompilationDto create(NewCompilationDto newCompilationDto);

    void delete(Long compId);

    void deleteEvent(Long compId, Long eventId);

    void addEvent(Long compId, Long eventId);

    void unpin(Long compId);

    void pin(Long compId);

}

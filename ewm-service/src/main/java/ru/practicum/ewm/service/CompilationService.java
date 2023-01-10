package ru.practicum.ewm.service;

import ru.practicum.ewm.dto.CompilationDto;
import ru.practicum.ewm.dto.NewCompilationDto;

import java.util.List;

public interface CompilationService {
    CompilationDto createCompilation(NewCompilationDto newCompilationDto);

    void deleteEventFromCompilation(Long compId, Long eventId);

    CompilationDto getCompilationById(Long compId);

    List<CompilationDto> getCompilations(Boolean pinned, Integer from, Integer size);

    NewCompilationDto addEventToCompilation(Long compId, Long eventId);

    void deletePinCompilation(Long compId);

    void pinCompilation(Long compId);

    void deleteCompilation(Long compId);
}

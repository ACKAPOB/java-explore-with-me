package ewmservice.compilation.service;

import ewmservice.compilation.dto.CompilationDto;
import ewmservice.compilation.dto.NewCompilationDto;

import java.util.List;

public interface CompilationService {
    CompilationDto saveCompilation(NewCompilationDto newCompilationDto);

    void deleteEventFromCompilation(Long compId, Long eventId);

    CompilationDto getCompilationById(Long compId);

    List<CompilationDto> getRequiredCompilations(Boolean pinned, Integer from, Integer size);

    NewCompilationDto addEventToCompilation(Long compId, Long eventId);

    void unPinCompilation(Long compId);

    void pinCompilation(Long compId);

    void deleteCompilation(Long compId);
}

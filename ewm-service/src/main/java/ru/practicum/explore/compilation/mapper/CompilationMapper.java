package ru.practicum.explore.compilation.mapper;

import ru.practicum.explore.compilation.dto.CompilationDto;
import ru.practicum.explore.compilation.dto.NewCompilationDto;
import ru.practicum.explore.event.dto.EventShortDto;
import ru.practicum.explore.compilation.model.Compilation;
import ru.practicum.explore.event.model.Event;

import java.util.List;

/**
 * Интерфейс маппера подбоки событий
 */
public interface CompilationMapper {
    /*
    Метод маппера из dto новой подборки в модель подборки
    */
    Compilation toCompilation(NewCompilationDto newCompilationDto, List<Event> events);

    /*
    Метод маппера из модели подборки событий в dto подборки
    */
    CompilationDto toCompilationDto(Compilation compilation, List<EventShortDto> list);
}

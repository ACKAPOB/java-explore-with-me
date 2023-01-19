package ru.practicum.explore.compilation.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.explore.compilation.dto.CompilationDto;
import ru.practicum.explore.compilation.dto.NewCompilationDto;
import ru.practicum.explore.event.dto.EventShortDto;
import ru.practicum.explore.compilation.model.Compilation;
import ru.practicum.explore.event.model.Event;

import java.util.List;

@Component
public class CompilationMapper {

    public Compilation toCompilation(NewCompilationDto compilationDto, List<Event> events) {
        return Compilation.builder()
                .events(events)
                .pinned(compilationDto.getPinned())
                .title(compilationDto.getTitle())
                .build();
    }

    public CompilationDto toCompilationDto(Compilation compilation, List<EventShortDto> list) {
        return CompilationDto.builder()
                .id(compilation.getId())
                .events(list)
                .pinned(compilation.getPinned())
                .title(compilation.getTitle())
                .build();
    }
}
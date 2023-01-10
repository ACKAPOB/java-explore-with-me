package ru.practicum.ewm.compilation.mapper;

import ru.practicum.ewm.compilation.dto.CompilationDto;
import ru.practicum.ewm.compilation.dto.NewCompilationDto;
import ru.practicum.ewm.compilation.model.Compilation;
import ru.practicum.ewm.event.model.Event;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
public class CompilationMapper {
    public static NewCompilationDto toNewCompilationDto(Compilation compilation) {
        return NewCompilationDto.builder()
                .id(compilation.getId())
                .title(compilation.getTitle())
                .events(compilation.getEvents().stream()
                        .map(Event::getId)
                        .collect(Collectors.toSet()))
                .pinned(compilation.getPinned())
                .build();
    }

    public static Compilation toCompilationFromNew(NewCompilationDto newCompilationDto, Set<Event> events) {
        return Compilation.builder()
                .id(newCompilationDto.getId())
                .title(newCompilationDto.getTitle())
                .pinned(newCompilationDto.getPinned())
                .events(events)
                .build();
    }

    public static CompilationDto toCompilationDto(Compilation compilation) {
        return CompilationDto.builder()
                .id(compilation.getId())
                .title(compilation.getTitle())
                .pinned(compilation.getPinned())
                .events(compilation.getEvents())
                .build();
    }
}

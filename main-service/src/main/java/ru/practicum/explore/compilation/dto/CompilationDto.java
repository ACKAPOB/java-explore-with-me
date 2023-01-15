package ru.practicum.explore.compilation.dto;

import lombok.*;
import ru.practicum.explore.event.dto.EventShortDto;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class CompilationDto {
    private Long id;
    private List<EventShortDto> events;
    private Boolean pinned;
    private String title;
}
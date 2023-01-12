package ru.practicum.explore.compilation.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NewCompilationDto {
    private Long id;
    private List<Long> events;
    private Boolean pinned;
    private String title;
}

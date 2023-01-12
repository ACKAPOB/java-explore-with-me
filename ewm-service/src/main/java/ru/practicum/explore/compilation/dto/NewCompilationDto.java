package ru.practicum.explore.compilation.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Dto для добовления новой подборки
 */
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

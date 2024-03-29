package ru.practicum.explore.compilation.dto;

import lombok.*;

import javax.validation.constraints.NotNull;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class NewCompilationDto {
    private List<Long> events;
    private Boolean pinned;
    @NotNull
    private String title;
}

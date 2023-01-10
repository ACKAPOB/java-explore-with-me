package ru.practicum.ewm.compilation.dto;

import ru.practicum.ewm.event.model.Event;
import lombok.*;

import javax.validation.constraints.NotBlank;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@EqualsAndHashCode(of = "id")
@Builder
public class CompilationDto {
    private Long id;
    private Boolean pinned;
    @NotBlank
    private String title;
    private Set<Event> events;
}

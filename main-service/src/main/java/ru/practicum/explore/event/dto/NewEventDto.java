package ru.practicum.explore.event.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.explore.location.model.Location;

import javax.validation.constraints.NotNull;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NewEventDto {
    @NotNull
    private String annotation;
    @NotNull
    private Long category;
    @NotNull
    private String description;
    @NotNull
    private String eventDate;
    @NotNull
    private Location location;
    private Boolean paid;
    private Integer participantLimit;
    private Boolean requestModeration;
    @NotNull
    private String title;
}

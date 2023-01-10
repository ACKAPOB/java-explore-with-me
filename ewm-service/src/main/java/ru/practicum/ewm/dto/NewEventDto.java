package ru.practicum.ewm.dto;

import ru.practicum.ewm.model.EventState;
import lombok.*;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@EqualsAndHashCode(of = "id")
@Builder
public class NewEventDto {
    private Long id;
    @NotBlank
    private String title;
    @NotBlank
    private String annotation;
    private Long category;
    @NotBlank
    private String description;
    @NotBlank
    private String eventDate;
    private Long initiator;
    private Location location;
    private Boolean paid;
    private EventState state;
    private LocalDateTime createdOn;
    private Integer participantLimit;
    private Boolean requestModeration;

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Location {
        private Float lat;
        private Float lon;
    }
}

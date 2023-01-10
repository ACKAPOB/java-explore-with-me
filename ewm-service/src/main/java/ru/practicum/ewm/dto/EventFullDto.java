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
public class EventFullDto {
    private Long id;
    @NotBlank
    private String title;
    @NotBlank
    private String annotation;
    private Category category;
    @NotBlank
    private String description;
    @NotBlank
    private String eventDate;
    private UserShortDto initiator;
    private Location location;
    private Boolean paid;
    private EventState state;
    private LocalDateTime createdOn;
    private LocalDateTime publishedOn;
    private Integer participantLimit;
    private Integer confirmedRequests;
    private Boolean requestModeration;
    private Long views;

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class UserShortDto {
        private Long id;
        @NotBlank
        private String name;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Location {
        private Float lat;
        private Float lon;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Category {
        private Long id;
        @NotBlank
        private String name;
    }
}

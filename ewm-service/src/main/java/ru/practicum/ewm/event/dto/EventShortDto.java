package ru.practicum.ewm.event.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@EqualsAndHashCode(of = "id")
@Builder
public class EventShortDto {
    private Long id;
    @NotBlank
    private String annotation;
    private CategoryDto category;
    private Integer confirmedRequests;
    @NotBlank
    private String eventDate;
    private UserShorDto initiator;
    private Boolean paid;
    @NotBlank
    private String title;
    private Long views;

    @AllArgsConstructor
    @NoArgsConstructor
    @Setter
    @Getter
    @EqualsAndHashCode(of = "id")
    @Builder
    public static class CategoryDto {
        private Long id;
        @NotBlank
        private String name;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Setter
    @Getter
    @EqualsAndHashCode(of = "id")
    @Builder
    public static class UserShorDto {
        private Long id;
        @NotBlank
        private String name;
    }
}

package ru.practicum.explore.dto;

import lombok.*;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ViewStatsDto {
    private String app;
    private String uri;
    private long hits;
}

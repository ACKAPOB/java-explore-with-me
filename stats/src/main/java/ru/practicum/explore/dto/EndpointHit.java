package ru.practicum.explore.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Dto для сохранения статистики
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EndpointHit {
    private Integer id;
    private String app;
    private String uri;
    private String ip;
    private String timestamp;
}

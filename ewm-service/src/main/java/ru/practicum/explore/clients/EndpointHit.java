package ru.practicum.explore.clients;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Dto для ендпоинта сохранения в stats-service
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

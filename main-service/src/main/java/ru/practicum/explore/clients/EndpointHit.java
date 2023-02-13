package ru.practicum.explore.clients;

import lombok.*;

@AllArgsConstructor
@Getter
@Setter
@ToString
public class EndpointHit {
    private String app;
    private String uri;
    private String ip;
}


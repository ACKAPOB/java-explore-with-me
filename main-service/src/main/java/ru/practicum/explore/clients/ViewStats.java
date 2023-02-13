package ru.practicum.explore.clients;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ViewStats {
    private String app;
    private String uri;
    private long hits;
}

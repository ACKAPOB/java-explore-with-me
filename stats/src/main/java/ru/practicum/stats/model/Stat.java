package ru.practicum.stats.model;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
@Builder
@Entity
@Table(name = "stats")
public class Stat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotBlank
    @NotNull
    @Column(name = "app")
    private String app;

    @NotBlank
    @NotNull
    @Column(name = "uri")
    private String uri;

    @NotBlank
    @NotNull
    @Column(name = "ip")
    private String ip;

    @Column(name = "request_time")
    private LocalDateTime timestamp;
}

package ru.practicum.explore.compilation.model;

import lombok.*;
import org.springframework.stereotype.Component;
import ru.practicum.explore.event.model.Event;

import javax.persistence.*;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "compilations")
@Builder
@Component
public class Compilation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(nullable = false)
    private Boolean pinned;
    @Column(nullable = false)
    private String title;
    @ManyToMany
    @JoinTable(name = "event_compilation",
            joinColumns = @JoinColumn(name = "compilation_id"),
            inverseJoinColumns = @JoinColumn(name = "event_id"))
    private List<Event> events;
}

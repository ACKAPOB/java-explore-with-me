package ru.practicum.explore.event.model;

import lombok.*;
import ru.practicum.explore.category.model.Category;
import ru.practicum.explore.location.model.Location;
import ru.practicum.explore.user.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "events")
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String annotation;
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "category_id")
    private Category category;
    private String description;
    @Column(name = "create_on")
    private LocalDateTime createdOn;
    @Column(name = "event_date")
    private LocalDateTime eventDate;
    @ManyToOne
    private Location location;
    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "initiator_id")
    private User initiator;
    private Boolean paid;
    @Column(name = "participant_limit")
    private Integer participantLimit;
    @Column(name = "published_on")
    private LocalDateTime publishedOn;
    @Column(name = "request_moderation")
    private Boolean requestModeration;
    private String title;
    @Enumerated(EnumType.STRING)
    @Column(name = "state")
    private Status state;
    @Column(name = "views")
    private Long views;
}

package ru.practicum.ewm.event.model;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
@Builder
@Entity
@Table(name = "events")
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "title", nullable = false, unique = true)
    private String title;

    @Column(name = "annotation", nullable = false)
    private String annotation;

    @JoinTable(name = "categories", joinColumns = @JoinColumn(name = "id"))
    @Column(name = "category_id", nullable = false)
    private Long category;

    @Column(name = "paid")
    private Boolean paid;

    @Column(name = "event_date", columnDefinition = "TIMESTAMP", nullable = false)
    private LocalDateTime eventDate;

    @JoinTable(name = "users", joinColumns = @JoinColumn(name = "id"))
    @Column(name = "initiator_id", nullable = false)
    private Long initiator;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "participant_limit")
    private Integer participantLimit;

    @Column(name = "confirmed_requests")
    private Integer confirmedRequests;

    @Column(name = "state")
    @Enumerated(EnumType.STRING)
    private EventState state;

    @Column(name = "published_on", columnDefinition = "TIMESTAMP")
    private LocalDateTime publishedOn;

    @Column(name = "created_on", columnDefinition = "TIMESTAMP")
    private LocalDateTime createdOn;

    @Column(name = "lat", nullable = false)
    private Float lat;

    @Column(name = "lon", nullable = false)
    private Float lon;

    @Column(name = "request_moderation")
    private Boolean requestModeration;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Event event = (Event) o;
        return id.equals(event.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}

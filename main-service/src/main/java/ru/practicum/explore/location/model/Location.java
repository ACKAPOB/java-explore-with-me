package ru.practicum.explore.location.model;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "locations")
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Location {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Float lat;
    private Float lon;
}

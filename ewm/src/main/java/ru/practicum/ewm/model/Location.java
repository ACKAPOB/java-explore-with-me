package main.java.ru.practicum.ewm.model;

import lombok.*;

import javax.persistence.*;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "locations")
public class Location {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "location_id")
    private long id;
    private double lat;
    private double lon;
}

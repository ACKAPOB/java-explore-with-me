package ru.practicum.explore.category.model;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "categories")
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
}

package ru.practicum.explore.user.model;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Email;

@Entity
@Table(name = "users")
@Data
@ToString
@RequiredArgsConstructor
@Builder
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "name", nullable = false, unique = true)
    private String name;
    @Email
    @Column(name = "email",nullable = false, unique = true)
    private String email;
}

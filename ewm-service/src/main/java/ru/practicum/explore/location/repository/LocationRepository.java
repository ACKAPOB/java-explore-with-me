package ru.practicum.explore.location.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.explore.location.model.Location;

/**
 * Интерфейс репозитория локаций
 */
public interface LocationRepository extends JpaRepository<Location, Long> {
}

package ru.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.model.Statistic;
import ru.practicum.model.ViewStats;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface StatRepository extends JpaRepository<Statistic, Long> {
    @Query("SELECT app AS app, uri AS uri, COUNT(ip) AS hits " +
            "FROM Statistic " +
            "WHERE :uris IS NULL OR uri IN :uris " +
            "AND timestamp BETWEEN :start AND :end " +
            "GROUP BY app, uri")
    List<ViewStats> getViews(LocalDateTime start, LocalDateTime end, List<String> uris);

    @Query("SELECT uri AS uri, app AS app, COUNT(DISTINCT ip) AS hits " +
            "FROM Statistic " +
            "WHERE :uris IS NULL OR uri IN :uris " +
            "AND timestamp BETWEEN :start AND :end " +
            "GROUP BY app, uri, ip")
    List<ViewStats> getUniqueViews(LocalDateTime start, LocalDateTime end, List<String> uris);
}

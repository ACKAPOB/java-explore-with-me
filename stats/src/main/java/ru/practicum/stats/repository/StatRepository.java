package ru.practicum.stats.repository;

import ru.practicum.stats.model.Stat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface StatRepository extends JpaRepository<Stat, Long> {

    @Query(value = "SELECT DISTINCT s.uri FROM Stat s " +
            "WHERE s.uri IN (:uris) AND s.timestamp>:start AND s.timestamp<:end")
    List<Stat> findAllByUniqUris(@Param("uris") List<String> uris,
                                 @Param("start") LocalDateTime start,
                                 @Param("end") LocalDateTime end);

    @Query("SELECT s FROM Stat s" +
            " WHERE s.uri IN (:uris) AND s.timestamp BETWEEN :start AND :end")
    List<Stat> findAllByUris(@Param("uris") List<String> uris,
                             @Param("start") LocalDateTime start,
                             @Param("end") LocalDateTime end);

    @Query("SELECT COUNT(s.uri) FROM Stat s WHERE s.uri=:uri ")
    Long countByUri(@Param("uri") String uri);
}

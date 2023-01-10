package ru.practicum.ewm.repository;

import ru.practicum.ewm.model.Compilation;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CompilationRepository extends JpaRepository<Compilation, Long> {
    @Query("SELECT c FROM Compilation c WHERE c.pinned = :pinned")
    List<Compilation> findAllByPinnedIsTrue(@Param("pinned") Boolean pinned, Pageable pageable);
}

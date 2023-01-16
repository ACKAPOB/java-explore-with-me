package ru.practicum.explore.compilation.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.explore.compilation.model.Compilation;

import java.util.List;

@Repository
public interface CompilationRepository extends JpaRepository<Compilation, Long> {

   // @Query("select c from Compilation c where c.pinned=?1 ORDER BY c.id")
  //  List<Compilation> findAllByPinnedOrderById(Boolean pinned, Pageable pageable);

    List<Compilation> findAllByPinned(boolean pinned, Pageable page);

}

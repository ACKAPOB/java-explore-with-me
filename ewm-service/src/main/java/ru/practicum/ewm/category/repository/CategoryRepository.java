package ru.practicum.ewm.category.repository;

import ru.practicum.ewm.category.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    //vb/,. .,mcv b,.v
    @Query(value = "SELECT * FROM categories c " +
            "LEFT JOIN events e on c.id = e.category_id " +
            "WHERE category_id=:id", nativeQuery = true)
    Category findEventLinkedWithCategory(@Param("id") Long catId);
}

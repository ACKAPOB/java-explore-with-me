package ewmservice.category.repository;

import ewmservice.category.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    @Query("SELECT c FROM Category c WHERE c.name=:name")
    Category findByName(@Param("name") String name);

    @Query(value = "SELECT * FROM categories c " +
            "LEFT JOIN events e on c.id = e.category_id " +
            "WHERE category_id=:id", nativeQuery = true)
    Category findEventLinkedWithCategory(@Param("id") Long catId);
}

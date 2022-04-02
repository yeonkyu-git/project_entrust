package project.entrust.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.entrust.entity.Category;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    Optional<Category> findByCategoryName(String categoryName);
}

package io.wisoft.vamos.repository;

import io.wisoft.vamos.domain.category.Category;
import io.wisoft.vamos.domain.category.CategoryName;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    Optional<Category> findByName(CategoryName name);
}

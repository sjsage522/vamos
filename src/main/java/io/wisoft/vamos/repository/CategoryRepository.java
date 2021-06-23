package io.wisoft.vamos.repository;

import io.wisoft.vamos.domain.category.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}

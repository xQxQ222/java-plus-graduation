package ru.yandex.practicum.repository.category;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.yandex.practicum.model.category.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    Boolean existsByNameIgnoreCase(String name);
}

package ru.yandex.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.yandex.practicum.model.Similarity;
import ru.yandex.practicum.model.SimilarityKey;

public interface EventSimilarityRepository extends JpaRepository<SimilarityKey, Similarity> {
}

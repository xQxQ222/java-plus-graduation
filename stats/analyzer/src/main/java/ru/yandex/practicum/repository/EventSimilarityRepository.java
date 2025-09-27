package ru.yandex.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.yandex.practicum.model.similarity.Similarity;
import ru.yandex.practicum.model.similarity.SimilarityKey;

import java.util.Optional;

public interface EventSimilarityRepository extends JpaRepository<Similarity, SimilarityKey> {
    Optional<Similarity> findByIdEventAIdAndIdEventBId(Long eventAId, Long eventBId);
}

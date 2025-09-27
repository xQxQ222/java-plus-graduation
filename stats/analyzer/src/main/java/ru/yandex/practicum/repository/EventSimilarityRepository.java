package ru.yandex.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.yandex.practicum.model.similarity.Similarity;
import ru.yandex.practicum.model.similarity.SimilarityKey;

import java.util.List;
import java.util.Optional;

public interface EventSimilarityRepository extends JpaRepository<Similarity, SimilarityKey> {
    Optional<Similarity> findByIdEventAIdAndIdEventBId(Long eventAId, Long eventBId);

    @Query("""
            SELECT s FROM Similarity s WHERE s.id.eventAId = :eventId OR s.id.eventBId = :eventId
            """)
    List<Similarity> findAssociatedWithEvent(Long eventId);
}

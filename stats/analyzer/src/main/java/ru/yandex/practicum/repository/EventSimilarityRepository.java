package ru.yandex.practicum.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.yandex.practicum.model.similarity.Similarity;
import ru.yandex.practicum.model.similarity.SimilarityKey;

import java.util.List;

public interface EventSimilarityRepository extends JpaRepository<Similarity, SimilarityKey> {
    @Query("""
            select s from Similarity s where s.id.eventAId = :eventId or s.id.eventBId = :eventId
            """)
    List<Similarity> findAssociatedWithEvent(Long eventId);

    @Query("""
            select s
            from Similarity s
            where s.id.eventAId in :ids or s.id.eventBId in :ids
            order by s.rating desc
            """)
    List<Similarity> findTopByEventIdsIn(List<Long> ids, Pageable pageable);
}

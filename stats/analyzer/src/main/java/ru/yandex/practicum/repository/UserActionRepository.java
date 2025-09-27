package ru.yandex.practicum.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.yandex.practicum.enums.UserActionType;
import ru.yandex.practicum.model.action.Action;

import java.util.List;
import java.util.Optional;

public interface UserActionRepository extends JpaRepository<Action, Long> {
    Optional<Action> findByUserIdAndEventIdAndActionType(Long userId, Long eventId, UserActionType actionType);

    List<Action> findByUserId(Long userId);

    List<Action> findByEventId(Long eventId);

    List<Action> findByEventIdIn(List<Long> eventIds);

    List<Long> findDistinctEventIdByUserIdOrderByTimestampDesc(Long userId);

    @Query("""
            select distinct a.eventId
            from Action a
            where a.userId = :userId
            order by a.timestamp desc
            """)
    List<Long> findDistinctEventIdsByUserIdOrderByTimestampDesc(Long userId, Pageable pageable);
}

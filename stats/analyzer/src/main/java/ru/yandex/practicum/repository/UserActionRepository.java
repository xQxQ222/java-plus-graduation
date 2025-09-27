package ru.yandex.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.yandex.practicum.enums.UserActionType;
import ru.yandex.practicum.model.action.Action;

import java.util.List;
import java.util.Optional;

public interface UserActionRepository extends JpaRepository<Action, Long> {
    Optional<Action> findByUserIdAndEventIdAndActionType(Long userId, Long eventId, UserActionType actionType);

    List<Action> findByUserId(Long userId);

    List<Action> findByEventId(Long eventId);
}

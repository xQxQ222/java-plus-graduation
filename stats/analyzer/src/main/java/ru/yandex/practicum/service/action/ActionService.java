package ru.yandex.practicum.service.action;

import ru.practicum.ewm.stats.avro.UserActionAvro;
import ru.yandex.practicum.model.action.Action;

import java.util.List;

public interface ActionService {
    void registerUserAction(UserActionAvro userActionAvro);

    List<Action> getUserActions(Long userId);

    List<Action> getActionsToEvent(Long eventId);
}

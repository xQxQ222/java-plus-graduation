package ru.yandex.practicum.service.action;

import ru.practicum.ewm.stats.avro.UserActionAvro;

public interface ActionService {
    void registerUserAction(UserActionAvro userActionAvro);
}

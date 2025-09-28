package ru.yandex.practicum.service;

import ru.yandex.practicum.grpc.stats.user.action.UserActionProto;

public interface UserActionService {
    void handleUserAction(UserActionProto userAction);
}

package ru.yandex.practicum.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.grpc.stats.user.action.UserActionProto;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserActionServiceImpl implements UserActionService {
    @Override
    public void handleUserAction(UserActionProto userAction) {

    }
}

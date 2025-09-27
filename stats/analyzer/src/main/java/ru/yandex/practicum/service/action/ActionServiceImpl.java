package ru.yandex.practicum.service.action;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.stats.avro.ActionTypeAvro;
import ru.practicum.ewm.stats.avro.UserActionAvro;
import ru.yandex.practicum.enums.UserActionType;
import ru.yandex.practicum.model.action.Action;
import ru.yandex.practicum.repository.UserActionRepository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ActionServiceImpl implements ActionService {

    private final UserActionRepository actionRepository;

    @Override
    public void registerUserAction(UserActionAvro userActionAvro) {
        Long userId = userActionAvro.getUserId();
        Long eventId = userActionAvro.getEventId();
        UserActionType actionType = getFromAvroActionType(userActionAvro.getActionType());

        Instant timestamp = userActionAvro.getTimestamp();

        Optional<Action> actionFromDb = actionRepository.findByUserIdAndEventIdAndActionType(userId, eventId, actionType);

        if (actionFromDb.isEmpty()) {
            Action newAction = Action.builder()
                    .eventId(eventId)
                    .userId(userId)
                    .actionType(actionType)
                    .timestamp(userActionAvro.getTimestamp())
                    .build();
            actionRepository.save(newAction);
        } else {
            Action action = actionFromDb.get();
            action.setTimestamp(timestamp);
            actionRepository.save(action);
        }
    }

    @Override
    public List<Action> getUserActions(Long userId) {
        return actionRepository.findByUserId(userId);
    }

    @Override
    public List<Action> getActionsToEvent(Long eventId) {
        return actionRepository.findByEventId(eventId);
    }


    private UserActionType getFromAvroActionType(ActionTypeAvro actionTypeAvro) {
        return switch (actionTypeAvro) {
            case LIKE -> UserActionType.LIKE;
            case REGISTER -> UserActionType.REGISTER;
            case VIEW -> UserActionType.VIEW;
            default -> throw new IllegalArgumentException("Неизвестный тип действия: " + actionTypeAvro.name());
        };
    }
}

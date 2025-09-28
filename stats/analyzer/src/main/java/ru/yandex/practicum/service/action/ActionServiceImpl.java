package ru.yandex.practicum.service.action;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.stats.avro.ActionTypeAvro;
import ru.practicum.ewm.stats.avro.UserActionAvro;
import ru.yandex.practicum.enums.UserActionType;
import ru.yandex.practicum.kafka.storage.util.ActionWeightStorage;
import ru.yandex.practicum.model.action.Action;
import ru.yandex.practicum.repository.UserActionRepository;

import java.time.Instant;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ActionServiceImpl implements ActionService {

    private final UserActionRepository actionRepository;
    private final ActionWeightStorage weightStorage;

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
            UserActionType userActionType = getFromAvroActionType(userActionAvro.getActionType());
            if (getActionWeight(action.getActionType()) > getActionWeight(userActionType)) {
                action.setTimestamp(userActionAvro.getTimestamp());
                action.setActionType(userActionType);
                actionRepository.save(action);
            }
        }
    }


    private UserActionType getFromAvroActionType(ActionTypeAvro actionTypeAvro) {
        return switch (actionTypeAvro) {
            case LIKE -> UserActionType.LIKE;
            case REGISTER -> UserActionType.REGISTER;
            case VIEW -> UserActionType.VIEW;
            default -> throw new IllegalArgumentException("Неизвестный тип действия: " + actionTypeAvro.name());
        };
    }

    private double getActionWeight(UserActionType actionType) {
        return switch (actionType) {
            case VIEW -> weightStorage.getView();
            case REGISTER -> weightStorage.getRegister();
            case LIKE -> weightStorage.getLike();
            default ->
                    throw new IllegalArgumentException("Неизвестный тип пользовательского действия: " + actionType.name());
        };
    }
}

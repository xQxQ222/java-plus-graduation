package ru.yandex.practicum.service;

import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.stats.avro.ActionTypeAvro;
import ru.practicum.ewm.stats.avro.UserActionAvro;
import ru.yandex.practicum.grpc.stats.user.action.ActionTypeProto;
import ru.yandex.practicum.grpc.stats.user.action.UserActionProto;
import ru.yandex.practicum.kafka.configuration.KafkaClient;
import ru.yandex.practicum.kafka.storage.name.TopicNames;
import ru.yandex.practicum.kafka.storage.properties.ProducerPropertiesStorage;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class UserActionServiceImpl implements UserActionService {
    private static final String PRODUCER_NAME = "collector";

    private final KafkaClient kafkaClient;
    private final TopicNames kafkaTopics;

    @Override
    public void handleUserAction(UserActionProto userActionProto) {
        UserActionAvro userActionAvro = toAvro(userActionProto);


        kafkaClient.getProducer(PRODUCER_NAME, ProducerPropertiesStorage.getCollectorProducerProperties())
                .send(new ProducerRecord<>(
                        kafkaTopics.getActions(),
                        null,
                        userActionAvro.getTimestamp().toEpochMilli(),
                        userActionProto.getEventId(),
                        userActionAvro
                ));
    }

    private ActionTypeAvro toActionTypeAvro(ActionTypeProto actionTypeProto) {
        return switch (actionTypeProto) {
            case ActionTypeProto.ACTION_VIEW -> ActionTypeAvro.VIEW;
            case ActionTypeProto.ACTION_REGISTER -> ActionTypeAvro.REGISTER;
            case ActionTypeProto.ACTION_LIKE -> ActionTypeAvro.LIKE;
            default -> throw new IllegalArgumentException("Неизвестный тип действия: " + actionTypeProto.name());
        };
    }

    private UserActionAvro toAvro(UserActionProto userActionProto) {
        Instant instant = Instant.ofEpochSecond(userActionProto.getTimestamp().getSeconds(), userActionProto.getTimestamp().getNanos());
        return UserActionAvro.newBuilder()
                .setActionType(toActionTypeAvro(userActionProto.getActionType()))
                .setUserId(userActionProto.getUserId())
                .setEventId(userActionProto.getEventId())
                .setTimestamp(instant)
                .build();
    }
}

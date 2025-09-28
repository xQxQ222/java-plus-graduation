package ru.yandex.practicum.client;

import com.google.protobuf.Timestamp;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.grpc.stats.user.action.ActionTypeProto;
import ru.yandex.practicum.grpc.stats.user.action.UserActionProto;
import ru.yandex.practicum.grpc.stats.user.action.controller.UserActionControllerGrpc;

import java.time.Instant;

@Component
public class CollectorGrpcClient {

    @GrpcClient("collector")
    private UserActionControllerGrpc.UserActionControllerBlockingStub actionClient;

    public void handleUserEventView(Long userId, Long eventId) {
        handleUserAction(userId, eventId, ActionTypeProto.ACTION_VIEW);
    }

    public void handleUserEventRegister(Long userId, Long eventId) {
        handleUserAction(userId, eventId, ActionTypeProto.ACTION_REGISTER);
    }

    public void handleUserEventLike(Long userId, Long eventId) {
        handleUserAction(userId, eventId, ActionTypeProto.ACTION_LIKE);
    }

    private void handleUserAction(Long userId, Long eventId, ActionTypeProto actionType) {
        Instant now = Instant.now();

        Timestamp timestamp = Timestamp.newBuilder()
                .setSeconds(now.getEpochSecond())
                .setNanos(now.getNano())
                .build();

        UserActionProto userActionProto = UserActionProto.newBuilder()
                .setUserId(userId)
                .setEventId(eventId)
                .setActionType(actionType)
                .setTimestamp(timestamp)
                .build();
        actionClient.collectUserAction(userActionProto);
    }
}

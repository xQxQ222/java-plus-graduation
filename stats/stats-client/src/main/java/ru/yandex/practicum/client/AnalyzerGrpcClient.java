package ru.yandex.practicum.client;

import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.grpc.stats.user.prediction.InteractionsCountRequestProto;
import ru.yandex.practicum.grpc.stats.user.prediction.RecommendedEventProto;
import ru.yandex.practicum.grpc.stats.user.prediction.SimilarEventsRequestProto;
import ru.yandex.practicum.grpc.stats.user.prediction.UserPredictionsRequestProto;
import ru.yandex.practicum.grpc.stats.user.prediction.controller.RecommendationsControllerGrpc;
import ru.yandex.practicum.util.IteratorConverter;

import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;


@Component
@Slf4j
public class AnalyzerGrpcClient {
    private final RecommendationsControllerGrpc.RecommendationsControllerBlockingStub recommendationClient;

    public AnalyzerGrpcClient(@GrpcClient("analyzer") RecommendationsControllerGrpc.RecommendationsControllerBlockingStub client) {
        recommendationClient = client;
    }

    public Stream<RecommendedEventProto> getRecommendationsForUser(long userId, int maxResults) {
        UserPredictionsRequestProto requestProto = UserPredictionsRequestProto.newBuilder()
                .setUserId(userId)
                .setMaxResults(maxResults)
                .build();
        Iterator<RecommendedEventProto> recommendations = recommendationClient.getRecommendationsForUser(requestProto);
        return IteratorConverter.toStream(recommendations);
    }

    public Stream<RecommendedEventProto> getSimilarEvents(long userId, long eventId, int maxResults) {
        SimilarEventsRequestProto requestProto = SimilarEventsRequestProto.newBuilder()
                .setUserId(userId)
                .setEventId(eventId)
                .setMaxResults(maxResults)
                .build();
        Iterator<RecommendedEventProto> recommendations = recommendationClient.getSimilarEvents(requestProto);
        return IteratorConverter.toStream(recommendations);
    }

    public Stream<RecommendedEventProto> getInteractionsCount(List<Long> eventsId) {
        InteractionsCountRequestProto requestProto = InteractionsCountRequestProto.newBuilder()
                .addAllEventId(eventsId)
                .build();
        Iterator<RecommendedEventProto> recommendations = recommendationClient.getInteractionsCount(requestProto);
        return IteratorConverter.toStream(recommendations);
    }
}

package ru.yandex.practicum.service;

import ru.yandex.practicum.grpc.stats.user.prediction.InteractionsCountRequestProto;
import ru.yandex.practicum.grpc.stats.user.prediction.RecommendedEventProto;
import ru.yandex.practicum.grpc.stats.user.prediction.SimilarEventsRequestProto;
import ru.yandex.practicum.grpc.stats.user.prediction.UserPredictionsRequestProto;

import java.util.List;

public interface AnalyzerService {
    List<RecommendedEventProto> getRecommendationsForUser(UserPredictionsRequestProto request);

    List<RecommendedEventProto> getSimilarEvents(SimilarEventsRequestProto request);

    List<RecommendedEventProto> getInteractionsCount(InteractionsCountRequestProto request);
}

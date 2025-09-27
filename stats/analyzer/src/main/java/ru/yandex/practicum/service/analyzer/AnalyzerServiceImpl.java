package ru.yandex.practicum.service.analyzer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.grpc.stats.user.prediction.InteractionsCountRequestProto;
import ru.yandex.practicum.grpc.stats.user.prediction.RecommendedEventProto;
import ru.yandex.practicum.grpc.stats.user.prediction.SimilarEventsRequestProto;
import ru.yandex.practicum.grpc.stats.user.prediction.UserPredictionsRequestProto;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AnalyzerServiceImpl implements AnalyzerService{
    @Override
    public List<RecommendedEventProto> getRecommendationsForUser(UserPredictionsRequestProto request) {
        return List.of();
    }

    @Override
    public List<RecommendedEventProto> getSimilarEvents(SimilarEventsRequestProto request) {
        return List.of();
    }

    @Override
    public List<RecommendedEventProto> getInteractionsCount(InteractionsCountRequestProto request) {
        return List.of();
    }
}

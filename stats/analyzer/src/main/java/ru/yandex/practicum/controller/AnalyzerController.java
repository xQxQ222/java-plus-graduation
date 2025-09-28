package ru.yandex.practicum.controller;

import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.service.GrpcService;
import ru.yandex.practicum.grpc.stats.user.prediction.InteractionsCountRequestProto;
import ru.yandex.practicum.grpc.stats.user.prediction.RecommendedEventProto;
import ru.yandex.practicum.grpc.stats.user.prediction.SimilarEventsRequestProto;
import ru.yandex.practicum.grpc.stats.user.prediction.UserPredictionsRequestProto;
import ru.yandex.practicum.grpc.stats.user.prediction.controller.RecommendationsControllerGrpc;
import ru.yandex.practicum.service.analyzer.AnalyzerService;

@RequiredArgsConstructor
@GrpcService
@Slf4j
public class AnalyzerController extends RecommendationsControllerGrpc.RecommendationsControllerImplBase {

    private final AnalyzerService analyzerService;

    @Override
    public void getRecommendationsForUser(UserPredictionsRequestProto request, StreamObserver<RecommendedEventProto> responseObserver) {
        log.info("Пришел запрос на GRPC контроллер - getRecommendationsForUser");
        try {
            analyzerService.getRecommendationsForUser(request).forEach(responseObserver::onNext);
            log.info("Получены рекомендации для пользователя по запросу: {}", request);
            responseObserver.onCompleted();
        } catch (Exception e) {
            responseObserver.onError(new StatusRuntimeException(
                    Status.INTERNAL
                            .withDescription(e.getLocalizedMessage())
                            .withCause(e)
            ));
        }
    }

    @Override
    public void getSimilarEvents(SimilarEventsRequestProto request, StreamObserver<RecommendedEventProto> responseObserver) {
        log.info("Пришел запрос на GRPC контроллер - getSimilarEvents");
        try {
            analyzerService.getSimilarEvents(request).forEach(responseObserver::onNext);
            log.info("Получены похожие мероприятия по запросу: {}", request);
            responseObserver.onCompleted();
        } catch (Exception e) {
            responseObserver.onError(new StatusRuntimeException(
                    Status.INTERNAL
                            .withDescription(e.getLocalizedMessage())
                            .withCause(e)
            ));
        }
    }

    @Override
    public void getInteractionsCount(InteractionsCountRequestProto request, StreamObserver<RecommendedEventProto> responseObserver) {
        log.info("Пришел запрос на GRPC контроллер - getInteractionsCount");
        try {
            analyzerService.getInteractionsCount(request).forEach(responseObserver::onNext);
            log.info("Получено количество итераций по запросу: {}", request);
            responseObserver.onCompleted();
        } catch (Exception e) {
            responseObserver.onError(new StatusRuntimeException(
                    Status.INTERNAL
                            .withDescription(e.getLocalizedMessage())
                            .withCause(e)
            ));
        }
    }
}
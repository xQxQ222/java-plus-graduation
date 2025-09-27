package ru.yandex.practicum.service.analyzer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.enums.UserActionType;
import ru.yandex.practicum.grpc.stats.user.prediction.InteractionsCountRequestProto;
import ru.yandex.practicum.grpc.stats.user.prediction.RecommendedEventProto;
import ru.yandex.practicum.grpc.stats.user.prediction.SimilarEventsRequestProto;
import ru.yandex.practicum.grpc.stats.user.prediction.UserPredictionsRequestProto;
import ru.yandex.practicum.kafka.storage.util.ActionWeightStorage;
import ru.yandex.practicum.model.action.Action;
import ru.yandex.practicum.model.similarity.Similarity;
import ru.yandex.practicum.repository.EventSimilarityRepository;
import ru.yandex.practicum.repository.UserActionRepository;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
@Slf4j
public class AnalyzerServiceImpl implements AnalyzerService {

    private final UserActionRepository actionRepository;
    private final EventSimilarityRepository similarityRepository;
    private final ActionWeightStorage weightStorage;

    @Override
    public List<RecommendedEventProto> getRecommendationsForUser(UserPredictionsRequestProto request) {
        return List.of();
    }

    @Override
    public List<RecommendedEventProto> getSimilarEvents(SimilarEventsRequestProto request) {
        long eventId = request.getEventId();
        long userId = request.getUserId();

        List<Similarity> associated = similarityRepository.findAssociatedWithEvent(eventId);

        Set<Long> allEventIds = associated.stream()
                .flatMap(s -> Stream.of(s.getId().getEventAId(), s.getId().getEventBId()))
                .collect(Collectors.toSet());

        List<Long> eventsWithUserAction = actionRepository.findDistinctEventIdByUserIdOrderByTimestampDesc(userId);

        return associated.stream()
                .filter(s -> !(eventsWithUserAction.contains(s.getId().getEventAId())
                        && eventsWithUserAction.contains(s.getId().getEventBId())))
                .sorted(Comparator.comparing(Similarity::getRating).reversed())
                .limit(request.getMaxResults())
                .map(s -> convertSimilarityToRecommendation(s, request.getEventId()))
                .toList();
    }

    @Override
    public List<RecommendedEventProto> getInteractionsCount(InteractionsCountRequestProto request) {
        List<Action> actionsByEvent = actionRepository.findByEventIdIn(request.getEventIdList());

        Map<Long, Double> maxWeightByEvent = actionsByEvent.stream()
                .collect(Collectors.groupingBy(Action::getEventId, Collectors.collectingAndThen(
                        Collectors.maxBy(Comparator.comparingDouble(a -> getUserActionWeight(a.getActionType()))),
                        opt -> opt.map(a -> getUserActionWeight(a.getActionType())).orElse(0.0))));


        return maxWeightByEvent.entrySet().stream()
                .map(e -> RecommendedEventProto.newBuilder()
                        .setEventId(e.getKey())
                        .setScore(e.getValue())
                        .build())
                .toList();
    }

    private double getUserActionWeight(UserActionType actionType) {
        return switch (actionType) {
            case UserActionType.VIEW -> weightStorage.getView();
            case UserActionType.REGISTER -> weightStorage.getRegister();
            case UserActionType.LIKE -> weightStorage.getLike();
            default -> throw new IllegalArgumentException("Неизвестный тип действия: " + actionType.name());
        };
    }

    private RecommendedEventProto convertSimilarityToRecommendation(Similarity similarity, Long requestEventId) {
        long eventId = similarity.getId().getEventAId().equals(requestEventId)
                ? similarity.getId().getEventBId()
                : similarity.getId().getEventAId();

        return RecommendedEventProto.newBuilder()
                .setEventId(eventId)
                .setScore(similarity.getRating())
                .build();
    }
}

package ru.yandex.practicum.storage;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.practicum.ewm.stats.avro.ActionTypeAvro;
import ru.practicum.ewm.stats.avro.EventSimilarityAvro;
import ru.practicum.ewm.stats.avro.UserActionAvro;
import ru.yandex.practicum.kafka.storage.util.ActionWeightStorage;

import java.time.Instant;
import java.util.*;

@Repository
@RequiredArgsConstructor
public class SimilarityStorageImpl implements SimilarityStorage {
    private final Map<Long, Map<Long, Double>> userActionsWeight = new HashMap<>();
    private final Map<Long, Map<Long, Double>> minWeightsSums = new HashMap<>();
    private final Map<Long, Double> eventWeightSums = new HashMap<>();
    private final Map<Long, Map<Long, List<EventSimilarityAvro>>> similaritiesForAction = new HashMap<>();
    private final ActionWeightStorage weightTable;


    @Override
    public boolean updateSimilarity(UserActionAvro userAction) {
        long userId = userAction.getUserId();
        long eventId = userAction.getEventId();

        Map<Long, Double> weightForEvent = userActionsWeight.computeIfAbsent(eventId, event -> new HashMap<>());

        double oldEventUserWeight = weightForEvent.getOrDefault(userId, 0.00);
        double newEventUserWeight = getWeightOfUserAction(userAction.getActionType());

        if (newEventUserWeight <= oldEventUserWeight) {
            return false;
        }
        weightForEvent.put(userId, newEventUserWeight);

        double oldSumForEvent = eventWeightSums.getOrDefault(eventId, 0.00);
        double newSumForEvent = oldSumForEvent - oldEventUserWeight + newEventUserWeight;
        eventWeightSums.put(eventId, newSumForEvent);

        boolean isUpdated = false;
        List<EventSimilarityAvro> similarityList = new ArrayList<>();

        for (Long otherEventId : userActionsWeight.keySet()) {
            if (otherEventId == eventId) {
                continue;
            }
            Map<Long, Double> otherEventWeights = userActionsWeight.get(otherEventId);
            if (!otherEventWeights.containsKey(userId)) {
                continue;
            }
            isUpdated = true;
            double newMinSum = updateMinSums(eventId, otherEventId, userId, oldEventUserWeight, newEventUserWeight);
            double similarity = calculateSimilarity(eventId, otherEventId, newMinSum);

            similarityList.add(createEventSimilarityAvro(eventId, otherEventId, similarity,
                    userAction.getTimestamp()));
        }

        Map<Long, List<EventSimilarityAvro>> similaritiesForEvent = similaritiesForAction.computeIfAbsent(userId, event -> new HashMap<>());
        similaritiesForEvent.put(eventId, similarityList);
        return isUpdated;
    }

    @Override
    public List<EventSimilarityAvro> getSimilarEvents(long eventId, long userId) {
        Instant timestamp = Instant.now();
        return similaritiesForAction.getOrDefault(userId, new HashMap<>()).getOrDefault(eventId, List.of());
    }

    private double getWeightOfUserAction(ActionTypeAvro actionType) {
        return switch (actionType) {
            case VIEW -> weightTable.getView();
            case REGISTER -> weightTable.getRegister();
            case LIKE -> weightTable.getLike();
            default -> 0;
        };
    }

    private double updateMinSums(Long eventA, Long eventB, Long userId, double oldWeight, double newWeight) {
        Map<Long, Double> eventBWeights = userActionsWeight.getOrDefault(userId, null);
        if (eventBWeights == null) {
            return 0;
        }
        double eventBWeightOld = eventBWeights.getOrDefault(userId, 0.00);

        double oldMinWeight = Math.min(eventBWeightOld, oldWeight);
        double newMinWeight = Math.min(eventBWeightOld, newWeight);

        return getMinSumForPair(eventA, eventB, oldMinWeight, newMinWeight);
    }

    private double getMinSumForPair(Long eventA, Long eventB, double oldMinWeight, double newMinWeight) {

        long firstEventId = Math.min(eventA, eventB);
        long secondEventId = Math.max(eventA, eventB);

        Map<Long, Double> minWeightsForFirstEvent = minWeightsSums.computeIfAbsent(firstEventId, ev -> new HashMap<>());
        double oldMinSum = minWeightsForFirstEvent.getOrDefault(secondEventId, 0.00);
        if (oldMinWeight == newMinWeight) {
            return oldMinSum;
        }
        double newMinSum = oldMinSum - oldMinWeight + newMinWeight;

        minWeightsForFirstEvent.put(secondEventId, newMinSum);
        return newMinSum;
    }

    private double calculateSimilarity(long eventA, long eventB, double minSum) {
        if (minSum == 0) {
            return 0;
        }
        double eventASum = eventWeightSums.getOrDefault(eventA, 0.00);
        double eventBSum = eventWeightSums.getOrDefault(eventB, 0.00);

        return (eventASum == 0 || eventBSum == 0) ? 0 : (minSum / (Math.sqrt(eventASum) * Math.sqrt(eventBSum)));
    }

    private EventSimilarityAvro createEventSimilarityAvro(long eventA, long eventB, double similarity, Instant timestamp) {

        long firstEventId = Math.min(eventA, eventB);
        long secondEventId = Math.max(eventA, eventB);

        return EventSimilarityAvro.newBuilder()
                .setEventA(firstEventId)
                .setEventB(secondEventId)
                .setScore(similarity)
                .setTimestamp(timestamp)
                .build();
    }
}

package ru.yandex.practicum.table;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.practicum.ewm.stats.avro.ActionTypeAvro;
import ru.yandex.practicum.config.ActionWeightTable;

import java.util.HashMap;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class RecommendationTableImpl implements RecommendationTable {
    private final Map<Long, Map<Long, Double>> weights = new HashMap<>();
    private final Map<Long, Map<Long, Double>> minWeightsSums = new HashMap<>();
    private final Map<Long, Double> eventWeightSums = new HashMap<>();
    private final ActionWeightTable weightTable;

    @Override
    public void put(long eventA, long eventB, double sum) {

    }

    @Override
    public double get(long eventA, long eventB) {
        return 0;
    }

    private double getWeightOfUserAction(ActionTypeAvro actionType) {
        return switch (actionType) {
            case VIEW -> weightTable.getView();
            case REGISTER -> weightTable.getRegister();
            case LIKE -> weightTable.getLike();
            default -> 0;
        };
    }
}

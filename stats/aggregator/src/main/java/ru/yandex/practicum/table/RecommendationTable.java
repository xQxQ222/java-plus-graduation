package ru.yandex.practicum.table;

public interface RecommendationTable {
    void put(long eventA, long eventB, double sum);

    double get(long eventA, long eventB);
}

package ru.yandex.practicum.service.similarity;

import ru.practicum.ewm.stats.avro.EventSimilarityAvro;

public interface SimilarityService {
    void addSimilarity(EventSimilarityAvro eventSimilarityAvro);
}

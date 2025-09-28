package ru.yandex.practicum.storage;

import ru.practicum.ewm.stats.avro.EventSimilarityAvro;
import ru.practicum.ewm.stats.avro.UserActionAvro;

import java.util.List;

public interface SimilarityStorage {
    List<EventSimilarityAvro> updateSimilarity(UserActionAvro userAction);
}

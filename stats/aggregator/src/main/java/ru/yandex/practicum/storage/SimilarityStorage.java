package ru.yandex.practicum.storage;

import ru.practicum.ewm.stats.avro.EventSimilarityAvro;
import ru.practicum.ewm.stats.avro.UserActionAvro;

import java.util.List;

public interface SimilarityStorage {
    boolean updateSimilarity(UserActionAvro userAction);

    List<EventSimilarityAvro> getSimilarEvents(long eventId, long userId);
}

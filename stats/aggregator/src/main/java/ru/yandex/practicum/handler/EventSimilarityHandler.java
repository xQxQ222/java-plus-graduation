package ru.yandex.practicum.handler;

import org.apache.avro.specific.SpecificRecordBase;
import ru.practicum.ewm.stats.avro.EventSimilarityAvro;
import ru.practicum.ewm.stats.avro.UserActionAvro;

import java.util.List;

public interface EventSimilarityHandler {
    List<EventSimilarityAvro> handleAction(SpecificRecordBase record);
}

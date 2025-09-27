package ru.yandex.practicum.deserializer;

import ru.practicum.ewm.stats.avro.EventSimilarityAvro;

public class EventSimilarityAvroDeserializer extends BaseAvroDeserializer<EventSimilarityAvro> {
    public EventSimilarityAvroDeserializer() {
        super(EventSimilarityAvro.getClassSchema());
    }
}

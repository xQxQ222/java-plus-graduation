package ru.yandex.practicum.deserializer;

import ru.practicum.ewm.stats.avro.UserActionAvro;

public class UserActionAvroDeserializer extends BaseAvroDeserializer<UserActionAvro>{
    public UserActionAvroDeserializer() {
        super(UserActionAvro.getClassSchema());
    }
}

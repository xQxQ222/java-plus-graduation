package ru.yandex.practicum.deserializer;

import org.apache.avro.Schema;
import org.apache.avro.io.BinaryDecoder;
import org.apache.avro.io.DatumReader;
import org.apache.avro.io.DecoderFactory;
import org.apache.avro.specific.SpecificDatumReader;
import org.apache.kafka.common.serialization.Deserializer;
import ru.yandex.practicum.exception.DeserializeException;

public abstract class BaseAvroDeserializer<T> implements Deserializer<T> {

    protected BinaryDecoder decoder;
    protected final DatumReader<T> datumWriter;

    public BaseAvroDeserializer(Schema schema) {
        this(DecoderFactory.get(), schema);
    }

    public BaseAvroDeserializer(DecoderFactory decoderFactory, Schema schema) {
        datumWriter = new SpecificDatumReader<>(schema);
    }

    @Override
    public T deserialize(String topic, byte[] data) {
        try {
            if (data == null) {
                return null;
            }
            decoder = DecoderFactory.get().binaryDecoder(data, null);
            return datumWriter.read(null, decoder);
        } catch (Exception e) {
            throw new DeserializeException("Ошибка десериализации данных из топика [\" + topic + \"]\", e");
        }
    }
}

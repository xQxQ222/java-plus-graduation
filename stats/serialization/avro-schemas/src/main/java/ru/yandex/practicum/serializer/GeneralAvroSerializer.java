package ru.yandex.practicum.serializer;

import org.apache.avro.io.BinaryEncoder;
import org.apache.avro.io.DatumWriter;
import org.apache.avro.io.EncoderFactory;
import org.apache.avro.specific.SpecificDatumWriter;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.common.serialization.Serializer;
import ru.yandex.practicum.exception.SerializeException;

import java.io.ByteArrayOutputStream;

public class GeneralAvroSerializer implements Serializer<SpecificRecordBase> {
    private final EncoderFactory decoderFactory = EncoderFactory.get();
    private BinaryEncoder encoder;

    @Override
    public byte[] serialize(String topic, SpecificRecordBase data) {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            if (data == null) {
                return null;
            }
            DatumWriter<SpecificRecordBase> datumWriter = new SpecificDatumWriter<>(data.getSchema());
            encoder = decoderFactory.binaryEncoder(outputStream, encoder);
            datumWriter.write(data, encoder);
            encoder.flush();
            return outputStream.toByteArray();
        } catch (Exception e) {
            throw new SerializeException("Ошибка сериализации данных для топика" + topic);
        }
    }
}

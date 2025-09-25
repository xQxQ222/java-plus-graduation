package ru.yandex.practicum.kafka.configuration;


import org.apache.avro.specific.SpecificRecord;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.consumer.Consumer;

import java.util.Properties;

public interface KafkaClient {
    Producer<String, SpecificRecord> getProducer(String producerName, Properties producerProperties);

    Consumer<String, SpecificRecordBase> getConsumer(String consumerName, Properties consumerProperties);

    void stopProducer(String producerName);

    void stopConsumer(String consumerName);
}

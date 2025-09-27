package ru.yandex.practicum.kafka.configuration;


import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.common.TopicPartition;

import java.util.Map;
import java.util.Properties;

public interface KafkaClient {
    Producer<Long, SpecificRecordBase> getProducer(String producerName, Properties producerProperties);

    Consumer<Long, SpecificRecordBase> getConsumer(String consumerName, Properties consumerProperties);

    void stopProducer(String producerName);

    void stopConsumer(String consumerName);

    void manageOffset(ConsumerRecord<Long, ? extends SpecificRecordBase> record, int count,
                      Consumer<Long, ? extends SpecificRecordBase> consumer,
                      Map<TopicPartition, OffsetAndMetadata> currentOffsets);
}

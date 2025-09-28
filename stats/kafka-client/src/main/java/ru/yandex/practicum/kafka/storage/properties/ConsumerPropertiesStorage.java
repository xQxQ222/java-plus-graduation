package ru.yandex.practicum.kafka.storage.properties;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.LongDeserializer;
import ru.yandex.practicum.deserializer.EventSimilarityAvroDeserializer;
import ru.yandex.practicum.deserializer.UserActionAvroDeserializer;

import java.util.Properties;

public class ConsumerPropertiesStorage {
    public static Properties getAggregatorConsumerProperties() {
        Properties properties = new Properties();
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, LongDeserializer.class);
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, UserActionAvroDeserializer.class);
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, "aggregator_consumers_group");
        return properties;
    }

    public static Properties getActionProcessorConsumerProperties() {
        Properties properties = new Properties();
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, LongDeserializer.class);
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, UserActionAvroDeserializer.class);
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, "analyzer_action_processor_group");
        return properties;
    }

    public static Properties getSimilarityProcessorConsumerProperties() {
        Properties properties = new Properties();
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, LongDeserializer.class);
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, EventSimilarityAvroDeserializer.class);
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, "analyzer_similarity_processor_group");
        return properties;
    }
}

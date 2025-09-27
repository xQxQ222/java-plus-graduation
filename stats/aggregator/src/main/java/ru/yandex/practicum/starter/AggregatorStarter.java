package ru.yandex.practicum.starter;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.errors.WakeupException;
import org.springframework.stereotype.Component;
import ru.practicum.ewm.stats.avro.EventSimilarityAvro;
import ru.yandex.practicum.handler.EventSimilarityHandler;
import ru.yandex.practicum.kafka.configuration.KafkaClient;
import ru.yandex.practicum.kafka.storage.name.TopicNames;
import ru.yandex.practicum.kafka.storage.properties.ConsumerPropertiesStorage;
import ru.yandex.practicum.kafka.storage.properties.ProducerPropertiesStorage;
import ru.yandex.practicum.utility.EventSimilarity;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Component
@Slf4j
@RequiredArgsConstructor
public class AggregatorStarter {

    private static final String CONSUMER_NAME = "aggregator-consumer";
    private static final String PRODUCER_NAME = "aggregator-consumer";
    private final static Duration POLL_DURATION_TIMEOUT = Duration.ofMillis(1000);

    private final KafkaClient kafkaClient;
    private final TopicNames topicsNames;
    private final EventSimilarityHandler eventSimilarityHandler;
    private final Map<TopicPartition, OffsetAndMetadata> currentOffsets = new HashMap<>();

    public void start() {
        Producer<Long, SpecificRecordBase> producer = kafkaClient.getProducer(PRODUCER_NAME, ProducerPropertiesStorage.getAggregatorProducerProperties());
        Consumer<Long, SpecificRecordBase> consumer = kafkaClient.getConsumer(CONSUMER_NAME, ConsumerPropertiesStorage.getAggregatorConsumerProperties());
        try {
            consumer.subscribe(List.of(topicsNames.getActions()));
            while (true) {
                ConsumerRecords<Long, SpecificRecordBase> records = consumer.poll(POLL_DURATION_TIMEOUT);
                int count = 0;
                for (ConsumerRecord<Long, SpecificRecordBase> record : records) {
                    List<EventSimilarityAvro> similarityAvroList = eventSimilarityHandler.handleAction(record.value());
                    sendToProducer(producer, similarityAvroList);
                    kafkaClient.manageOffset(record, count, consumer, currentOffsets);
                    count++;
                }
                consumer.commitAsync();
            }
        } catch (WakeupException ignored) {
        } catch (Exception e) {
            log.error("Ошибка во время обработки событий от датчиков", e);
        } finally {

            try {
                consumer.commitSync();
            } finally {
                log.info("Закрываем consumer + producer");
                kafkaClient.stopConsumer(CONSUMER_NAME);
            }
        }
    }

    private void sendToProducer(Producer<Long, SpecificRecordBase> producer, List<EventSimilarityAvro> similarityAvroList) {
        if (!similarityAvroList.isEmpty()) {
            for (EventSimilarityAvro eventSimilarityAvro : similarityAvroList) {
                ProducerRecord<Long, SpecificRecordBase> producerRecord = new ProducerRecord<>(
                        topicsNames.getSimilarity(),
                        null,
                        eventSimilarityAvro.getTimestamp().toEpochMilli(),
                        eventSimilarityAvro.getEventA(),
                        eventSimilarityAvro
                );
                producer.send(producerRecord);
            }
        }
    }
}

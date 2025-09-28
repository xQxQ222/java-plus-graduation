package ru.yandex.practicum.processor;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.errors.WakeupException;
import org.springframework.stereotype.Component;
import ru.practicum.ewm.stats.avro.UserActionAvro;
import ru.yandex.practicum.kafka.configuration.KafkaClient;
import ru.yandex.practicum.kafka.storage.name.TopicNames;
import ru.yandex.practicum.kafka.storage.properties.ConsumerPropertiesStorage;
import ru.yandex.practicum.service.action.ActionService;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Slf4j
@Component
public class ActionProcessor implements Runnable {

    private final static Duration POLL_DURATION_TIMEOUT = Duration.ofMillis(1000);
    private final static String ACTION_PROCESSOR_CONSUMER = "action-processor";

    private final KafkaClient kafkaClient;
    private final TopicNames topicNames;
    private final ActionService actionService;
    private final Map<TopicPartition, OffsetAndMetadata> currentOffsets = new HashMap<>();

    private Consumer<Long, SpecificRecordBase> consumer;

    @Override
    public void run() {
        try {
            consumer = kafkaClient.getConsumer(ACTION_PROCESSOR_CONSUMER, ConsumerPropertiesStorage.getActionProcessorConsumerProperties());
            consumer.subscribe(List.of(topicNames.getActions()));

            while (true) {
                ConsumerRecords<Long, SpecificRecordBase> records = consumer.poll(POLL_DURATION_TIMEOUT);
                int count = 0;
                for (ConsumerRecord<Long, SpecificRecordBase> record : records) {
                    UserActionAvro userActionAvro = (UserActionAvro) record.value();
                    actionService.registerUserAction(userActionAvro);
                    kafkaClient.manageOffset(record, count, consumer, currentOffsets);
                    count++;
                }
                consumer.commitAsync();
            }
        } catch (WakeupException ignored) {
        } catch (Exception e) {
            log.error("Ошибка во время обработки действий", e);
        } finally {
            try {
                consumer.commitSync(currentOffsets);
            } finally {
                log.info("Закрытие consumer");
                kafkaClient.stopConsumer(ACTION_PROCESSOR_CONSUMER);
            }
        }
    }
}

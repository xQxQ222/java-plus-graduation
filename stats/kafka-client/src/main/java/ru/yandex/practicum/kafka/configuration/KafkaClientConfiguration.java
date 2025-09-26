package ru.yandex.practicum.kafka.configuration;

import jakarta.annotation.PreDestroy;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.common.TopicPartition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@Getter
@Setter
@Configuration
@Slf4j
public class KafkaClientConfiguration {

    @Bean
    KafkaClient getClient() {
        return new KafkaClient() {
            private final Map<String, Producer<Long, SpecificRecordBase>> producerMap = new HashMap<>();

            private final Map<String, Consumer<Long, SpecificRecordBase>> consumerMap = new HashMap<>();


            @Override
            public Producer<Long, SpecificRecordBase> getProducer(String producerName, Properties producerProperties) {
                if (!producerMap.containsKey(producerName)) {
                    createProducer(producerName, producerProperties);
                }
                return producerMap.get(producerName);
            }

            @Override
            public Consumer<Long, SpecificRecordBase> getConsumer(String consumerName, Properties consumerProperties) {
                if (!consumerMap.containsKey(consumerName)) {
                    createConsumer(consumerName, consumerProperties);
                }
                return consumerMap.get(consumerName);
            }

            @Override
            public void stopProducer(String producerName) {
                if (producerMap.containsKey(producerName)) {
                    Producer<Long, SpecificRecordBase> producer = producerMap.get(producerName);
                    if (producer != null) {
                        producer.flush();
                        producer.close();
                    }
                }
            }

            @Override
            public synchronized void stopConsumer(String consumerName) {
                if (consumerMap.containsKey(consumerName)) {
                    Consumer<Long, SpecificRecordBase> consumer = consumerMap.get(consumerName);
                    if (consumer != null) {
                        consumer.close();
                    }
                }
            }

            @Override
            public void manageOffset(ConsumerRecord<String, ? extends SpecificRecordBase> record, int count, Consumer<String, ? extends SpecificRecordBase> consumer, Map<TopicPartition, OffsetAndMetadata> currentOffsets) {
                currentOffsets.put(
                        new TopicPartition(record.topic(), record.partition()),
                        new OffsetAndMetadata(record.offset() + 1)
                );

                if (count % 10 == 0) {
                    consumer.commitAsync(currentOffsets, (offsets, exception) -> {
                        if (exception != null) {
                            log.warn("Ошибка при попытке коммита offset {}", offsets, exception);
                        }
                    });
                }
            }


            private void createProducer(String name, Properties properties) {
                Producer<Long, SpecificRecordBase> producer = new KafkaProducer<>(properties);
                producerMap.put(name, producer);
            }

            private void createConsumer(String name, Properties properties) {
                Consumer<Long, SpecificRecordBase> consumer = new KafkaConsumer<>(properties);
                Runtime.getRuntime().addShutdownHook(new Thread(consumer::wakeup));
                consumerMap.put(name, consumer);
            }
        };
    }
}

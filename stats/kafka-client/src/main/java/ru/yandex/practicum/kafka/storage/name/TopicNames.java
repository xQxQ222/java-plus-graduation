package ru.yandex.practicum.kafka.storage.name;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "kafka.topic.stats")
public class TopicNames {
    private String sensors;
    private String hubs;
    private String snapshots;
}

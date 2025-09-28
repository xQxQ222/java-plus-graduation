package ru.yandex.practicum.kafka.storage.util;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "action.weight")
public class ActionWeightStorage {
    private double view;
    private double register;
    private double like;
}

package ru.yandex.practicum.utility;

import lombok.*;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EventSimilarity {
    private long eventId;
    private Map<Long, Double> similarityToOtherEvents = new HashMap<>();
}

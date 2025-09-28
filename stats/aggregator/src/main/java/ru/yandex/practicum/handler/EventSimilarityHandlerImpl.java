package ru.yandex.practicum.handler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.specific.SpecificRecordBase;
import org.springframework.stereotype.Component;
import ru.practicum.ewm.stats.avro.EventSimilarityAvro;
import ru.practicum.ewm.stats.avro.UserActionAvro;
import ru.yandex.practicum.storage.SimilarityStorage;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class EventSimilarityHandlerImpl implements EventSimilarityHandler {

    private final SimilarityStorage similarityStorage;

    @Override
    public List<EventSimilarityAvro> handleAction(SpecificRecordBase userAction) {
        try {
            UserActionAvro userActionAvro = (UserActionAvro) userAction;
            return similarityStorage.updateSimilarity(userActionAvro);
        } catch (Exception e) {
            log.error("Ошибка приведения информации из топика к типу UserActionAvro");
            throw new IllegalArgumentException("Ошибка приведения информации из топика к типу UserActionAvro");
        }

    }
}

package ru.yandex.practicum.service.similarity;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.stats.avro.EventSimilarityAvro;
import ru.yandex.practicum.model.similarity.Similarity;
import ru.yandex.practicum.model.similarity.SimilarityKey;
import ru.yandex.practicum.repository.EventSimilarityRepository;

import java.util.Optional;

@RequiredArgsConstructor
@Slf4j
@Service
public class SimilarityServiceImpl implements SimilarityService {

    private final EventSimilarityRepository similarityRepository;

    @Override
    public void addSimilarity(EventSimilarityAvro eventSimilarityAvro) {
        long eventAId = eventSimilarityAvro.getEventA();
        long eventBId = eventSimilarityAvro.getEventB();

        Similarity similarity = Similarity.builder()
                .id(new SimilarityKey(eventAId, eventBId))
                .timestamp(eventSimilarityAvro.getTimestamp())
                .rating(eventSimilarityAvro.getScore())
                .build();
        similarityRepository.save(similarity);
    }
}

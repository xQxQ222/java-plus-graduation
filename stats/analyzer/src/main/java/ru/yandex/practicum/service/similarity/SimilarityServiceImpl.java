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

        Optional<Similarity> similarityFromDb = similarityRepository.findByIdEventAIdAndIdEventBId(eventAId, eventBId);

        if (similarityFromDb.isEmpty()) {
            SimilarityKey similarityKey = new SimilarityKey(eventAId, eventBId);
            Similarity similarityNew = Similarity.builder()
                    .id(similarityKey)
                    .rating(eventSimilarityAvro.getScore())
                    .timestamp(eventSimilarityAvro.getTimestamp())
                    .build();
            similarityRepository.save(similarityNew);
        } else {
            Similarity similarity = similarityFromDb.get();
            similarity.setRating(eventSimilarityAvro.getScore());
            similarity.setTimestamp(eventSimilarityAvro.getTimestamp());
            similarityRepository.save(similarity);
        }
    }
}

package ru.yandex.practicum.feign.fallback;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import ru.practicum.stats.dto.EndpointHitDto;
import ru.practicum.stats.dto.ViewStatsDto;
import ru.yandex.practicum.exception.ServiceUnavailableException;
import ru.yandex.practicum.feign.api.StatsApi;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class StatsFeignFallback implements StatsApi {

    private static final String SERVICE_NAME = "stats-server";

    @Override
    public ResponseEntity<List<ViewStatsDto>> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique) {
        throw new ServiceUnavailableException(SERVICE_NAME);
    }

    @Override
    public ResponseEntity<String> hitStat(EndpointHitDto hitDto) {
        throw new ServiceUnavailableException(SERVICE_NAME);
    }
}

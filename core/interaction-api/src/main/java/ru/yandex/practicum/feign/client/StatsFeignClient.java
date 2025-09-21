package ru.yandex.practicum.feign.client;

import org.springframework.cloud.openfeign.FeignClient;
import ru.yandex.practicum.feign.api.StatsApi;
import ru.yandex.practicum.feign.fallback.StatsFeignFallback;

@FeignClient(name = "stats-server", fallback = StatsFeignFallback.class)
public interface StatsFeignClient extends StatsApi {
}

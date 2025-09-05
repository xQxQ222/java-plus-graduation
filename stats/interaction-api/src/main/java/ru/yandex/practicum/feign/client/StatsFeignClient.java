package ru.yandex.practicum.feign.client;

import org.springframework.cloud.openfeign.FeignClient;
import ru.yandex.practicum.feign.api.StatsApi;
import ru.yandex.practicum.feign.client.config.FeignConfig;

@FeignClient(name = "stats-server", configuration = FeignConfig.class)
public interface StatsFeignClient extends StatsApi {
}

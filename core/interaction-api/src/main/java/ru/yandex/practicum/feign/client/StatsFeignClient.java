package ru.yandex.practicum.feign.client;

import org.springframework.cloud.openfeign.FeignClient;
import ru.yandex.practicum.feign.api.StatsApi;

@FeignClient(name = "stats-server")
public interface StatsFeignClient extends StatsApi {
}

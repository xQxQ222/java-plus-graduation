package ru.yandex.practicum.feign.client;

import org.springframework.cloud.openfeign.FeignClient;
import ru.yandex.practicum.feign.api.EventApi;
import ru.yandex.practicum.feign.fallback.EventFeignFallback;

@FeignClient(name = "event-service", path = "/utility/events", fallback = EventFeignFallback.class)
public interface EventFeignClient extends EventApi {
}

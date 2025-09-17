package ru.yandex.practicum.feign.client;

import org.springframework.cloud.openfeign.FeignClient;
import ru.yandex.practicum.feign.api.EventApi;

@FeignClient(name = "event-service", path = "/events")
public interface EventFeignClient extends EventApi {
}

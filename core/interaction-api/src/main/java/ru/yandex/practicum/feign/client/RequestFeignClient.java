package ru.yandex.practicum.feign.client;

import org.springframework.cloud.openfeign.FeignClient;
import ru.yandex.practicum.feign.api.RequestApi;
import ru.yandex.practicum.feign.fallback.RequestFeignFallback;

@FeignClient(value = "request-service", path = "/utility/requests", fallback = RequestFeignFallback.class)
public interface RequestFeignClient extends RequestApi {
}

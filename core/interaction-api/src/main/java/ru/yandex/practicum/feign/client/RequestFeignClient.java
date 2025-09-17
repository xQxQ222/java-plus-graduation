package ru.yandex.practicum.feign.client;

import org.springframework.cloud.openfeign.FeignClient;
import ru.yandex.practicum.feign.api.RequestApi;

@FeignClient(value = "request-service", path = "/requests")
public interface RequestFeignClient extends RequestApi {
}

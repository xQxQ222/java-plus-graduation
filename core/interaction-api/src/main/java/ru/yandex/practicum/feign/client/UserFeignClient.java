package ru.yandex.practicum.feign.client;

import org.springframework.cloud.openfeign.FeignClient;
import ru.yandex.practicum.feign.api.UserApi;
import ru.yandex.practicum.feign.fallback.UserFeignFallback;

@FeignClient(name = "user-service", path = "/utility/users", fallback = UserFeignFallback.class)
public interface UserFeignClient extends UserApi {

}

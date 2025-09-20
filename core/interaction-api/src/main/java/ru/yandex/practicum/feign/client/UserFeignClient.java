package ru.yandex.practicum.feign.client;

import org.springframework.cloud.openfeign.FeignClient;
import ru.yandex.practicum.feign.api.UserApi;

@FeignClient(name = "user-service", path = "/utility/users")
public interface UserFeignClient extends UserApi {

}

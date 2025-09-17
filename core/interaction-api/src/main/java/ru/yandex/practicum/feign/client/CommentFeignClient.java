package ru.yandex.practicum.feign.client;

import org.springframework.cloud.openfeign.FeignClient;
import ru.yandex.practicum.feign.api.CommentApi;

@FeignClient(name = "comment-service", path = "/events")
public interface CommentFeignClient extends CommentApi {
}

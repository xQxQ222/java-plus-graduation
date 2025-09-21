package ru.yandex.practicum.feign.client;

import org.springframework.cloud.openfeign.FeignClient;
import ru.yandex.practicum.feign.api.CommentApi;
import ru.yandex.practicum.feign.fallback.CommentFeignFallback;

@FeignClient(name = "comment-service", path = "/utility/comments", fallback = CommentFeignFallback.class)
public interface CommentFeignClient extends CommentApi {
}

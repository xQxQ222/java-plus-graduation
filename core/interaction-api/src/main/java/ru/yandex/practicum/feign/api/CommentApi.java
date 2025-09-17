package ru.yandex.practicum.feign.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.dto.comment.CommentDto;
import ru.yandex.practicum.dto.comment.GetCommentDto;

import java.util.List;
import java.util.Set;

public interface CommentApi {
    @GetMapping("/events/last")
    List<GetCommentDto> getLastCommentsForEvents(@RequestBody Set<Long> eventsId);

    @GetMapping("/event/{eventId}")
    List<GetCommentDto> getCommentsByEventId(@PathVariable Long eventId);
}

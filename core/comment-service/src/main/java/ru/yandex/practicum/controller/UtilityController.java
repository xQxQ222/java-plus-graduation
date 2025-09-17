package ru.yandex.practicum.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.dto.comment.CommentDto;
import ru.yandex.practicum.dto.comment.GetCommentDto;
import ru.yandex.practicum.enums.comment.CommentSortType;
import ru.yandex.practicum.service.CommentService;

import java.util.List;
import java.util.Set;

@Slf4j
@RestController
@RequestMapping("/comments")
@RequiredArgsConstructor
public class UtilityController {

    private final CommentService commentService;

    @GetMapping("/events/last")
    public List<GetCommentDto> getLastCommentsForEvents(@RequestBody Set<Long> eventsId) {
        log.info("Пришел GET запрос на /comments/events/last с телом: {}", eventsId);
        List<GetCommentDto> comments = commentService.getCommentsByEventsIds(eventsId);
        log.info("Для мероприятий с id ({}) найдены следующие комментарии: {}", eventsId, comments);
        return comments;
    }

    @GetMapping("/event/{eventId}")
    public List<GetCommentDto> getCommentsByEventId(@PathVariable Long eventId) {
        log.info("Пришел GET запрос на /comments/event/{}", eventId);
        List<GetCommentDto> comments = commentService.getEventComments(eventId, 0, Integer.MAX_VALUE, CommentSortType.COMMENTS_NEW);
        log.info("Для мероприятия с id {} нашлись комментарии: {}", eventId, comments);
        return comments;
    }
}

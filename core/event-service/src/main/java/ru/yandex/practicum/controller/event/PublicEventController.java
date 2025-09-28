package ru.yandex.practicum.controller.event;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.dto.event.EventFullDto;
import ru.yandex.practicum.dto.event.EventShortDto;
import ru.yandex.practicum.enums.event.EventSortType;
import ru.yandex.practicum.exception.BadRequestException;
import ru.yandex.practicum.service.event.EventService;
import ru.yandex.practicum.service.event.param.GetEventUserParam;

import java.time.LocalDateTime;
import java.util.List;

import static ru.yandex.practicum.utility.Constants.DATE_PATTERN;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/events")
@Validated
public class PublicEventController {

    private final EventService eventService;


    @GetMapping
    public ResponseEntity<List<EventShortDto>> getEventsByFilters(@RequestParam(name = "text", required = false) String text,
                                                                  @RequestParam(name = "categories", required = false) List<Long> categories,
                                                                  @RequestParam(name = "paid", required = false) Boolean paid,
                                                                  @RequestParam(name = "rangeStart", required = false) @DateTimeFormat(pattern = DATE_PATTERN) LocalDateTime rangeStart,
                                                                  @RequestParam(name = "rangeEnd", required = false) @DateTimeFormat(pattern = DATE_PATTERN) LocalDateTime rangeEnd,
                                                                  @RequestParam(name = "onlyAvailable", defaultValue = "false") Boolean onlyAvailable,
                                                                  @RequestParam(name = "sort", required = false) EventSortType sort,
                                                                  @RequestParam(name = "from", defaultValue = "0") @Min(0) Integer from,
                                                                  @RequestParam(name = "size", defaultValue = "10") @Min(1) Integer size,
                                                                  HttpServletRequest request) {
        if (rangeStart != null && rangeEnd != null && rangeEnd.isBefore(rangeStart)) {
            throw new BadRequestException("rangeStart > rangeEnd");
        }
        log.info("Пришел GET запрос /events на Public Event Controller");

        Pageable page;
        if (sort != null) {
            Sort sortType = switch (sort) {
                case EVENT_DATE -> Sort.by("createdOn").ascending();
                case VIEWS -> Sort.by("views").ascending();
            };
            page = PageRequest.of(from, size, sortType);
        } else {
            page = PageRequest.of(from, size);
        }

        GetEventUserParam param = GetEventUserParam.builder()
                .text(text)
                .categories(categories)
                .paid(paid)
                .rangeStart(rangeStart)
                .rangeEnd(rangeEnd)
                .onlyAvailable(onlyAvailable)
                .page(page)
                .build();

        List<EventShortDto> events = eventService.getEventsByUser(param);
        log.info("Отправлен ответ на GET /events Public Event Controller с телом: {}", events);
        return ResponseEntity.ok(events);
    }

    @GetMapping("/{eventId}")
    public ResponseEntity<EventFullDto> getEventById(@RequestHeader("X-EWM-USER-ID") long userId, @PathVariable Long eventId, HttpServletRequest request) {
        log.info("Пришел GET запрос на /events/{} Public Event Controller", eventId);
        EventFullDto event = eventService.getEventById(userId, eventId);
        log.info("Отправлен ответ на GET /events/{} c телом: {}", eventId, event);
        return ResponseEntity.ok(event);
    }

    @PutMapping("/{eventId}/like")
    public void putLikeToEvent(@RequestHeader("X-EWM-USER-ID") long userId, @PathVariable long eventId) {
        log.info("Пришел PUT запрос на /events/{}/like", eventId);
        log.info("Отправлен ответ на запрос PUT /events/{}/like", eventId);
        eventService.putLikeToEvent(userId, eventId);
    }

    @GetMapping("/recommendations")
    public List<EventFullDto> getRecommendations(@RequestHeader("X-EWM-USER-ID") long userId) {
        log.info("Пришел GET запрос на /events/recommendations");
        List<EventFullDto> recommendations = eventService.getRecommendations(userId);
        log.info("Отправлен ответ на запрос GET /events/recommendations");
        return recommendations;
    }
}

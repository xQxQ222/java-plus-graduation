package ru.yandex.practicum.util;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.dto.comment.GetCommentDto;
import ru.yandex.practicum.dto.event.EventFullDto;
import ru.yandex.practicum.dto.event.EventShortDto;
import ru.yandex.practicum.dto.event.ResponseEvent;
import ru.yandex.practicum.dto.request.ConfirmedRequests;
import ru.yandex.practicum.enums.request.RequestStatus;
import ru.practicum.stats.dto.ViewStatsDto;
import ru.yandex.practicum.feign.client.CommentFeignClient;
import ru.yandex.practicum.feign.client.RequestFeignClient;
import ru.yandex.practicum.feign.client.StatsFeignClient;
import ru.yandex.practicum.mapper.event.MapperEvent;
import ru.yandex.practicum.model.event.Event;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static ru.yandex.practicum.utility.Constants.DEFAULT_COMMENTS;
import static ru.yandex.practicum.utility.Constants.MIN_START_DATE;

@Component
@RequiredArgsConstructor
@Slf4j
public class ResponseEventBuilder {
    private final MapperEvent eventMapper;
    private final RequestFeignClient requestFeignClient;
    private final CommentFeignClient commentFeignClient;
    private final StatsFeignClient statsClient;

    public <T extends ResponseEvent> T buildOneEventResponseDto(Event event, Class<T> type) {
        T dto;

        if (type == EventFullDto.class) {
            EventFullDto dtoTemp = eventMapper.toEventFullDto(event);
            dto = type.cast(dtoTemp);
        } else {
            EventShortDto dtoTemp = eventMapper.toEventShortDto(event);
            dto = type.cast(dtoTemp);
        }

        long eventId = event.getId();
        LocalDateTime created = event.getCreatedOn();

        dto.setConfirmedRequests(getOneEventConfirmedRequests(eventId));
        dto.setViews(getOneEventViews(created, eventId));
        dto.setComments(getOneEventComments(eventId));
        return dto;
    }

    public <T extends ResponseEvent> List<T> buildManyEventResponseDto(List<Event> events, Class<T> type) {
        Map<Long, T> dtoById = new HashMap<>();

        for (Event event : events) {
            if (type == EventFullDto.class) {
                EventFullDto dtoTemp = eventMapper.toEventFullDto(event);
                dtoById.put(event.getId(), type.cast(dtoTemp));
            } else {
                EventShortDto dtoTemp = eventMapper.toEventShortDto(event);
                dtoById.put(event.getId(), type.cast(dtoTemp));
            }
        }

        getManyEventsConfirmedRequests(dtoById.keySet()).forEach(req ->
                dtoById.get(req.eventId()).setConfirmedRequests(req.countRequests()));


        getManyEventsViews(dtoById.keySet()).forEach(stats -> {
            Long id = Long.parseLong(stats.getUri().replace("/events/", ""));
            dtoById.get(id).setViews(stats.getHits());
        });

        getManyEventsComments(dtoById.keySet()).forEach(comment -> {
            T t = dtoById.get(comment);

            if (t.getComments() == null) {
                t.setComments(new ArrayList<>());
            }

            t.getComments().add(comment);
        });

        return new ArrayList<>(dtoById.values());
    }

    private int getOneEventConfirmedRequests(long eventId) {
        return requestFeignClient.getRequestsCountByEventIdAndStatus(eventId, RequestStatus.CONFIRMED);
    }

    private long getOneEventViews(LocalDateTime created, long eventId) {
        List<ViewStatsDto> viewStats = statsClient.getStats(created.minusMinutes(1), LocalDateTime.now().plusMinutes(1), List.of("/events/" + eventId), true).getBody();
        return viewStats.isEmpty() ? 0 : viewStats.getFirst().getHits();
    }

    private List<GetCommentDto> getOneEventComments(long eventId) {
        return commentFeignClient.getCommentsByEventId(eventId);
    }

    private List<ConfirmedRequests> getManyEventsConfirmedRequests(Collection<Long> eventIds) {
        return requestFeignClient.getConfirmedRequestsByEventId(eventIds);
    }

    private List<ViewStatsDto> getManyEventsViews(Collection<Long> eventIds) {
        List<String> uris = eventIds.stream()
                .map(id -> "/events/" + id)
                .toList();

        List<ViewStatsDto> viewStats = statsClient.getStats(MIN_START_DATE, LocalDateTime.now().plusMinutes(1), uris, true).getBody();
        return viewStats;
    }

    private List<GetCommentDto> getManyEventsComments(Set<Long> eventsIds) {
        return commentFeignClient.getLastCommentsForEvents(eventsIds);
    }
}

package ru.yandex.practicum.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.dto.request.ConfirmedRequests;
import ru.yandex.practicum.dto.request.ParticipationRequestDto;
import ru.yandex.practicum.enums.request.RequestStatus;
import ru.yandex.practicum.feign.api.RequestApi;
import ru.yandex.practicum.service.RequestService;

import java.util.Collection;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/utility/requests")
@RequiredArgsConstructor
public class UtilityRequestController implements RequestApi {

    private final RequestService requestService;

    @GetMapping("/{eventId}")
    public List<ParticipationRequestDto> getRequestsByEventId(@PathVariable Long eventId) {
        log.info("Пришел GET запрос на /requests/{}", eventId);
        List<ParticipationRequestDto> requests = requestService.getRequestsByEventId(eventId);
        log.info("Получен список заявок на мероприятие с id {}: {}", eventId, requests);
        return requests;
    }

    @GetMapping("/{eventId}/count")
    public int getRequestsCountByEventIdAndStatus(@PathVariable Long eventId, @RequestParam(name = "status") RequestStatus status) {
        log.info("Пришел GET апрос на /requests/{}/count с телом: {}", eventId, status);
        int count = requestService.getRequestsCountByEventIdAndStatus(eventId, status);
        log.info("Для мероприятия с id {} найдено {} заявок со статусом {}", eventId, count, status);
        return count;
    }

    @GetMapping("/confirmed")
    public List<ConfirmedRequests> getConfirmedRequestsByEventId(@RequestParam(name = "ids") Collection<Long> eventsIds) {
        log.info("Пришел GET запрос на /requests/confirmed с телом: {}", eventsIds);
        List<ConfirmedRequests> confirmedRequests = requestService.getConfirmedRequestsByEventId(eventsIds);
        log.info("Для мероприятий с id ({}) нашлись комментарии: одобренные заявки: {}", eventsIds, confirmedRequests);
        return confirmedRequests;
    }
}

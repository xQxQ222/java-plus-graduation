package ru.yandex.practicum.feign.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import ru.yandex.practicum.dto.request.ConfirmedRequests;
import ru.yandex.practicum.dto.request.ParticipationRequestDto;
import ru.yandex.practicum.enums.request.RequestStatus;

import java.util.Collection;
import java.util.List;

public interface RequestApi {
    @GetMapping("/{eventId}")
    List<ParticipationRequestDto> getRequestsByEventId(@PathVariable Long eventId);

    @GetMapping("/{eventId}/count")
    int getRequestsCountByEventIdAndStatus(@PathVariable Long eventId, @RequestParam(name = "status") RequestStatus status);

    @GetMapping("/confirmed")
    List<ConfirmedRequests> getConfirmedRequestsByEventId(@RequestParam(name = "ids") Collection<Long> eventsIds);
}

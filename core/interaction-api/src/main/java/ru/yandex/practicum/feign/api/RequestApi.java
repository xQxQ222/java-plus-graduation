package ru.yandex.practicum.feign.api;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.dto.request.ConfirmedRequests;
import ru.yandex.practicum.dto.request.ParticipationRequestDto;
import ru.yandex.practicum.enums.request.RequestStatus;

import java.util.Collection;
import java.util.List;

public interface RequestApi {
    @GetMapping("/event/{eventId}")
    List<ParticipationRequestDto> getRequestsByEventId(@PathVariable Long eventId);

    @GetMapping("/event/{eventId}/count")
    int getRequestsCountByEventIdAndStatus(@PathVariable Long eventId, @RequestParam(name = "status") RequestStatus status);

    @GetMapping("/confirmed")
    List<ConfirmedRequests> getConfirmedRequestsByEventId(@RequestParam(name = "ids") Collection<Long> eventsIds);

    @PutMapping("/{requestId}/confirm")
    ParticipationRequestDto updateRequestStatus(@PathVariable Long requestId, @RequestBody RequestStatus status);

    @GetMapping("/users/{userId}/event/{eventId}")
    ParticipationRequestDto getUserEventRequest(@PathVariable(name = "userId") Long userId, @PathVariable(name = "eventId") Long eventId);
}

package ru.yandex.practicum.feign.fallback;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.dto.request.ConfirmedRequests;
import ru.yandex.practicum.dto.request.ParticipationRequestDto;
import ru.yandex.practicum.enums.request.RequestStatus;
import ru.yandex.practicum.exception.ServiceUnavailableException;
import ru.yandex.practicum.feign.api.RequestApi;

import java.util.Collection;
import java.util.List;

@Component
public class RequestFeignFallback implements RequestApi {

    private static final String SERVICE_NAME = "request-service";

    @Override
    public List<ParticipationRequestDto> getRequestsByEventId(Long eventId) {
        throw new ServiceUnavailableException(SERVICE_NAME);
    }

    @Override
    public int getRequestsCountByEventIdAndStatus(Long eventId, RequestStatus status) {
        throw new ServiceUnavailableException(SERVICE_NAME);
    }

    @Override
    public List<ConfirmedRequests> getConfirmedRequestsByEventId(Collection<Long> eventsIds) {
        throw new ServiceUnavailableException(SERVICE_NAME);
    }

    @Override
    public ParticipationRequestDto updateRequestStatus(Long requestId, RequestStatus status) {
        throw new ServiceUnavailableException(SERVICE_NAME);
    }

    @Override
    public ParticipationRequestDto getUserEventRequest(Long userId, Long eventId) {
        throw new ServiceUnavailableException(SERVICE_NAME);
    }
}

package ru.yandex.practicum.feign.fallback;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.dto.event.EventFullDto;
import ru.yandex.practicum.exception.ServiceUnavailableException;
import ru.yandex.practicum.feign.api.EventApi;

@Component
public class EventFeignFallback implements EventApi {

    private static final String SERVICE_NAME = "event-service";

    @Override
    public ResponseEntity<EventFullDto> getEventById(Long eventId) {
        throw new ServiceUnavailableException(SERVICE_NAME);
    }
}

package ru.yandex.practicum.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.dto.event.EventFullDto;
import ru.yandex.practicum.dto.request.ConfirmedRequests;
import ru.yandex.practicum.dto.user.UserDto;
import ru.yandex.practicum.enums.event.EventState;
import ru.yandex.practicum.exception.ConflictException;
import ru.yandex.practicum.exception.DuplicateException;
import ru.yandex.practicum.exception.NotFoundException;
import ru.yandex.practicum.dto.request.ParticipationRequestDto;
import ru.yandex.practicum.enums.request.RequestStatus;
import ru.yandex.practicum.feign.client.EventFeignClient;
import ru.yandex.practicum.feign.client.UserFeignClient;
import ru.yandex.practicum.mapper.MapperRequest;
import ru.yandex.practicum.model.Request;
import ru.yandex.practicum.repository.RequestRepository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RequestServiceImpl implements RequestService {
    private final RequestRepository requestRepository;
    private final EventFeignClient eventFeignClient;
    private final UserFeignClient userFeignClient;
    private final MapperRequest mapperRequest;

    @Override
    public List<ParticipationRequestDto> getParticipationRequests(Long userId) {
        List<Request> requests = requestRepository.findAllByRequesterId(userId);
        return requests.stream().map(mapperRequest::toParticipationRequestDto).toList();
    }

    @Override
    @Transactional
    public ParticipationRequestDto createParticipationRequest(Long userId, Long eventId) {

        EventFullDto event = eventFeignClient.getEventById(eventId).getBody();

        UserDto user = userFeignClient.getUserById(userId);

        if (requestRepository.existsByEventIdAndRequesterId(eventId, userId)) {
            throw new DuplicateException("Запрос на такое событие уже есть");
        }

        if (!event.getState().equals(EventState.PUBLISHED)) {
            throw new ConflictException("Невозможно создать запрос на неопубликованное событие");
        }


        if (event.getParticipantLimit() != 0 && requestRepository.countByEventIdAndStatus(eventId, RequestStatus.CONFIRMED)
                >= event.getParticipantLimit()) {
            throw new ConflictException("Достигнут лимит запросов на событие");
        }

        if (event.getInitiator().getId().equals(userId)) {
            throw new ConflictException("Невозможно создать запрос будучи инициатором события");
        }

        boolean isPreModerationOn = isPreModerationOn(event.getRequestModeration(), event.getParticipantLimit());
        Request request = new Request(
                null,
                userId,
                eventId,
                isPreModerationOn ? RequestStatus.PENDING : RequestStatus.CONFIRMED,
                LocalDateTime.now()
        );

        request = requestRepository.save(request);

        return mapperRequest.toParticipationRequestDto(request);
    }

    @Override
    @Transactional
    public ParticipationRequestDto cancelParticipationRequest(Long userId, Long requestId) {
        Request request = requestRepository.findById(requestId).orElseThrow(() ->
                new NotFoundException("Запрос не найден"));

        userFeignClient.getUserById(userId); //Будет проверять, существует ли пользователь

        request.setStatus(RequestStatus.CANCELED);

        request = requestRepository.save(request);

        return mapperRequest.toParticipationRequestDto(request);
    }

    @Override
    public List<ParticipationRequestDto> getRequestsByEventId(Long eventId) {
        return requestRepository.findAllByEventId(eventId).stream()
                .map(mapperRequest::toParticipationRequestDto)
                .toList();
    }

    @Override
    public int getRequestsCountByEventIdAndStatus(Long eventId, RequestStatus status) {
        return requestRepository.countByEventIdAndStatus(eventId, status);
    }

    @Override
    public List<ConfirmedRequests> getConfirmedRequestsByEventId(Collection<Long> eventIds) {
        return requestRepository.getConfirmedRequests(eventIds, RequestStatus.CONFIRMED);
    }

    private boolean isPreModerationOn(boolean moderationStatus, int limit) {
        return moderationStatus && limit != 0;
    }
}

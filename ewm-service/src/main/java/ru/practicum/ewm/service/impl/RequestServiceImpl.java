package ru.practicum.ewm.service.impl;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.model.Event;
import ru.practicum.ewm.model.EventState;
import ru.practicum.ewm.repository.EventRepository;
import ru.practicum.ewm.exception.NotFoundException;
import ru.practicum.ewm.exception.ValidationException;
import ru.practicum.ewm.dto.RequestDto;
import ru.practicum.ewm.mapper.RequestMapper;
import ru.practicum.ewm.model.Request;
import ru.practicum.ewm.model.RequestState;
import ru.practicum.ewm.repository.RequestRepository;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.service.RequestService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static ru.practicum.ewm.mapper.RequestMapper.toRequest;
import static ru.practicum.ewm.mapper.RequestMapper.toRequestDto;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class RequestServiceImpl implements RequestService {
    private final RequestRepository requestRepository;
    private final EventRepository eventRepository;

    @Override
    @Transactional
    public RequestDto createRequest(Long userId, Long eventId) {
        log.info("Добавление запроса от текущего пользователя на участие в событии userId = {}, " +
                "eventId = {}, RequestServiceImpl.createRequest", userId, eventId);
        validateRequestSave(userId, eventId);
        RequestDto requestDto = RequestDto.builder()
                .requester(userId)
                .event(eventId)
                .status(RequestState.PENDING)
                .created(LocalDateTime.now())
                .build();
        try {
            return toRequestDto(requestRepository.save(toRequest(requestDto)));
        } catch (RuntimeException e) {
            throw new ValidationException("Вы уже сделали запрос на участие в данном событии.");
        }
    }

    @Override
    @Transactional
    public RequestDto cancelRequestByRequester(Long userId, Long reqId) {
        log.info("Отмена своего запроса на участие в событии RequestServiceImpl.cancelRequestByRequester, " +
                "reqId={}, userId={}", reqId, userId);
        Request request = requestRepository.findRequestForRequester(userId, reqId);
        request.setStatus(RequestState.CANCELED);
        requestRepository.save(request);
        return toRequestDto(request);
    }

    @Override
    @Transactional
    public List<RequestDto> getForUserHisRequests(Long userId) {
        log.info("Получение информации о заявках текущего пользователя на участие в чужих событиях userId = {}," +
                "RequestServiceImpl.getForUserHisRequests", userId);
        return requestRepository.findAllByRequester(userId).stream()
                .map(RequestMapper::toRequestDto)
                .collect(Collectors.toList());
    }

    private void validateRequestSave(Long userId, Long eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException(String.format("События с id %d не существует", eventId)));
        if (!event.getRequestModeration())
            if (Objects.equals(event.getInitiator(), userId))
                throw new ValidationException("Организатор не может стать участником события.");
            if (!event.getState().equals(EventState.PUBLISHED))
                throw new ValidationException("Событие еще не опубликовано.");
            if ((event.getParticipantLimit() - event.getConfirmedRequests()) <= 0)
                throw new ValidationException("Достигнут максимум участников.");
    }
}

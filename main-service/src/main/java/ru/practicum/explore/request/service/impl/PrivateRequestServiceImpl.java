package ru.practicum.explore.request.service.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explore.event.model.Event;
import ru.practicum.explore.event.model.Status;
import ru.practicum.explore.event.repository.EventRepository;
import ru.practicum.explore.exception.ErrorRequestException;
import ru.practicum.explore.exception.ObjectNotFoundException;
import ru.practicum.explore.exception.ValidationException;
import ru.practicum.explore.request.dto.RequestDto;
import ru.practicum.explore.request.model.Request;
import ru.practicum.explore.request.model.StatusRequest;
import ru.practicum.explore.request.repository.RequestRepository;
import ru.practicum.explore.request.service.PrivateRequestService;
import ru.practicum.explore.user.model.User;
import ru.practicum.explore.user.repository.UserRepository;
import ru.practicum.explore.request.mapper.RequestMapper;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Slf4j
@AllArgsConstructor
public class PrivateRequestServiceImpl implements PrivateRequestService {

    private final RequestRepository requestRepository;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;

    @Override
    public List<RequestDto> get(Long userId) {
        log.info("Получение информации о заявках текущего пользователя на участие в чужих событиях. userId = {} " +
                "PrivateRequestServiceImpl.get", userId);
        userRepository
                .findById(userId)
                .orElseThrow(() -> new ObjectNotFoundException(String.format("Пользовтель не найден getRequestsByUser " +
                        "id = %s", userId)));
        return requestRepository.findAllByRequester_IdOrderById(userId)
                .stream()
                .map(RequestMapper::toRequestDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public RequestDto post(Long userId, Long eventId) {
        log.info("Добавление запроса от текущего пользователя на участие в событии userId = {} and eventId = {} " +
                "PrivateRequestServiceImpl.post", userId, eventId);
        if (eventId == null) {
            log.info("Добавление запроса от текущего eventId == null");
            throw new ValidationException(String.format("Событие не найдено postRequest id = %s", userId));
        }
        if (Objects.equals(eventRepository.findById(eventId)
                .orElseThrow(() -> new ObjectNotFoundException(String.format("Событие не найдено postRequest" +
                        "id = %s", userId)))
                .getInitiator().getId(), userId)) {
            throw new ErrorRequestException("Ошибка Вы не инициатор");
        }
        if (eventRepository.findById(eventId)
                .orElseThrow(() -> new ObjectNotFoundException(String.format("Событие не найдено postRequest" +
                        "id = %s", userId))).getState().equals(Status.PUBLISHED)) {
            User user = userRepository
                    .findById(userId)
                    .orElseThrow(() -> new ObjectNotFoundException(String.format("Пользователь не найден postRequest" +
                            "id = %s", userId)));
            Event event = eventRepository
                    .findById(eventId)
                    .orElseThrow(() -> new ObjectNotFoundException(String.format("Событие не найдено " +
                            "postRequest id = %s", eventId)));
            new Request();
            Request request = Request.builder()
                    .created(LocalDateTime.now())
                    .event(event)
                    .requester(user)
                    .status(StatusRequest.PENDING)
                    .build();

            Integer limitParticipant = requestRepository.countByEvent_IdAndStatus(eventId, StatusRequest.CONFIRMED);
            if (!event.getRequestModeration()) {
                request.setStatus(StatusRequest.CONFIRMED);
            }
            if (event.getParticipantLimit() != 0 && Objects.equals(event.getParticipantLimit(), limitParticipant)) {
                request.setStatus(StatusRequest.REJECTED);
            }
            return RequestMapper.toRequestDto(requestRepository.save(request));
        } else {
            throw new ErrorRequestException("Ошибка при добавлении запроса PrivateRequestServiceImpl.post");
        }
    }

    @Override
    @Transactional
    public RequestDto cancelRequest(Long userId, Long requestId) {
        log.info("Отмена своего запроса на участие в событии userId = {}, requestId = {} " +
                "PrivateRequestServiceImpl.cancelRequest", userId, requestId);
        userRepository
                .findById(userId)
                .orElseThrow(() -> new ObjectNotFoundException(String.format("Пользователь не найден " +
                        "cancelRequest id = %s", userId)));

        if (!Objects.equals(requestRepository.findById(requestId).get().getRequester().getId(), userId)) {
            throw new ErrorRequestException("Ошибка вы не инициатор cancelRequest");
        }
        Request request = requestRepository
                .findById(requestId)
                .orElseThrow(() -> new ObjectNotFoundException(String.format("Запрос не найден " +
                        "cancelRequest id = %s", requestId)));
        request.setStatus(StatusRequest.CANCELED);
        return RequestMapper.toRequestDto(requestRepository.save(request));
    }
}

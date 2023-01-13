package ru.practicum.explore.request.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.explore.event.model.Event;
import ru.practicum.explore.event.model.Status;
import ru.practicum.explore.event.repository.EventRepository;
import ru.practicum.explore.exception.ErrorRequestException;
import ru.practicum.explore.exception.ObjectNotFoundException;
import ru.practicum.explore.request.dto.RequestDto;
import ru.practicum.explore.request.mapper.model.Request;
import ru.practicum.explore.request.model.StatusRequest;
import ru.practicum.explore.request.repository.RequestRepository;
import ru.practicum.explore.request.service.RequestService;
import ru.practicum.explore.user.model.User;
import ru.practicum.explore.user.repository.UserRepository;
import ru.practicum.explore.request.mapper.RequestMapper;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Slf4j

public class RequestServiceImpl implements RequestService {

    private final RequestRepository requestRepository;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;

    public RequestServiceImpl(RequestRepository requestRepository, EventRepository eventRepository, UserRepository userRepository) {
        this.requestRepository = requestRepository;
        this.eventRepository = eventRepository;
        this.userRepository = userRepository;
    }

    @Override
    public List<RequestDto> getRequestsByUser(Long userId) {
        userRepository
                .findById(userId)
                .orElseThrow(() -> new ObjectNotFoundException(String.format("User not found id = %s", userId)));
        return requestRepository.findAllByRequester_IdOrderById(userId).stream()
                .map(RequestMapper::toRequestDto)
                .collect(Collectors.toList());
    }

    @Override
    public RequestDto postRequestUser(Long userId, Long eventId) {
        if (eventRepository.findById(eventId).isEmpty()) {
            throw new ObjectNotFoundException(String.format("postRequestUser Event not found id = %s", userId));
        }
        if (Objects.equals(eventRepository.findById(eventId).get().getInitiator().getId(), userId)) {
            throw new ErrorRequestException("postRequestUser Sorry you no Event initiator");
        }
        if (eventRepository.findById(eventId).get().getState().equals(Status.PUBLISHED)) {
            User user = userRepository
                    .findById(userId)
                    .orElseThrow(() -> new ObjectNotFoundException(String.format("postRequestUser " +
                            "User not found id = %s", userId)));
            Event event = eventRepository
                    .findById(eventId)
                    .orElseThrow(() -> new ObjectNotFoundException(String.format("postRequestUser " +
                            "Event not found id = %s", eventId)));
            new Request();
            Request request = Request.builder()
                    .created(LocalDateTime.now())
                    .event(event)
                    .requester(user)
                    .status(StatusRequest.PENDING)
                    .build();

            Integer limitParticipant = requestRepository.countByEvent_IdAndStatus(eventId,
                    StatusRequest.CONFIRMED);

            if (!event.getRequestModeration()) {
                request.setStatus(StatusRequest.CONFIRMED);
            }
            if (event.getParticipantLimit() != 0 && Objects.equals(event.getParticipantLimit(), limitParticipant)) {
                request.setStatus(StatusRequest.REJECTED);
            }
            return RequestMapper.toRequestDto(requestRepository.save(request));
        } else {
            throw new ErrorRequestException("Sorry you no Event no published");
        }
    }

    @Override
    public RequestDto cancelRequestByUser(Long userId, Long requestId) {
        userRepository
                .findById(userId)
                .orElseThrow(() -> new ObjectNotFoundException(String.format("cancelRequestByUser User not found id = %s", userId)));

        if (!Objects.equals(requestRepository.findById(requestId).get().getRequester().getId(), userId)) {
            throw new ErrorRequestException("cancelRequestByUser Sorry you no Event initiator");
        }
        Request request = requestRepository
                .findById(requestId)
                .orElseThrow(() -> new ObjectNotFoundException(String.format("cancelRequestByUser Request not found id = %s", requestId)));
        request.setStatus(StatusRequest.CANCELED);
        return RequestMapper.toRequestDto(requestRepository.save(request));
    }
}

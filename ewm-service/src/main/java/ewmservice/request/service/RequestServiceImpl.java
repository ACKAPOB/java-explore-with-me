package ewmservice.request.service;

import ewmservice.event.model.Event;
import ewmservice.event.model.EventState;
import ewmservice.event.repository.EventRepository;
import ewmservice.exception.EntityNotFoundException;
import ewmservice.exception.ValidationException;
import ewmservice.request.dto.RequestDto;
import ewmservice.request.mapper.RequestMapper;
import ewmservice.request.model.Request;
import ewmservice.request.model.RequestState;
import ewmservice.request.repository.RequestRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static ewmservice.request.mapper.RequestMapper.toRequest;
import static ewmservice.request.mapper.RequestMapper.toRequestDto;

@Service
public class RequestServiceImpl implements RequestService {
    private final RequestRepository requestRepository;
    private final EventRepository eventRepository;

    public RequestServiceImpl(RequestRepository requestRepository, EventRepository eventRepository) {
        this.requestRepository = requestRepository;
        this.eventRepository = eventRepository;
    }


    @Override
    public RequestDto saveRequest(Long userId, Long eventId) {
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
    public RequestDto confirmRequest(Long userId, Long eventId, Long reqId) {
        Request request = requestRepository
                .findByInitiatorAndRequestAndEvent(reqId, userId, eventId);
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() ->
                        new EntityNotFoundException(String.format("События с id %d не существует", eventId)));
        if (!event.getRequestModeration() || event.getParticipantLimit() == 0
                || (event.getParticipantLimit() - event.getConfirmedRequests()) != 0) {
            request.setStatus(RequestState.CONFIRMED);
            event.setConfirmedRequests(event.getConfirmedRequests() + 1);
            eventRepository.save(event);
        } else {
            request.setStatus(RequestState.REJECTED);
        }
        return toRequestDto(requestRepository.save(request));
    }

    @Override
    public RequestDto rejectRequestByOwner(Long userId, Long eventId, Long reqId) {
        Request request = requestRepository
                .findByInitiatorAndRequestAndEvent(reqId, userId, eventId);
        request.setStatus(RequestState.REJECTED);
        return toRequestDto(requestRepository.save(request));
    }

    @Override
    public RequestDto cancelRequestByRequester(Long userId, Long reqId) {
        Request request = requestRepository.findRequestForRequester(userId, reqId);
        request.setStatus(RequestState.CANCELED);
        requestRepository.save(request);
        return toRequestDto(request);
    }

    @Override
    public List<RequestDto> getForUserHisRequests(Long userId) {
        return requestRepository.findAllByRequester(userId).stream()
                .map(RequestMapper::toRequestDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<RequestDto> getInfoAboutRequestsForEventOwner(Long userId, Long eventId) {
        return requestRepository.getInfoAboutRequestsForEventOwner(userId, eventId).stream()
                .map(RequestMapper::toRequestDto)
                .collect(Collectors.toList());
    }

    private void validateRequestSave(Long userId, Long eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() ->
                        new EntityNotFoundException(String.format("События с id %d не существует", eventId)));
        if (!event.getRequestModeration()) {
            if (Objects.equals(event.getInitiator(), userId)) {
                throw new ValidationException("Организатор не может стать участником события.");
            }
            if (!event.getState().equals(EventState.PUBLISHED)) {
                throw new ValidationException("Событие еще не опубликовано.");
            }
            if ((event.getParticipantLimit() - event.getConfirmedRequests()) <= 0) {
                throw new ValidationException("Достигнут максимум участников.");
            }
        }
    }
}
